package fr.parisrespire.mpp.data

import com.github.florent37.log.Logger
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.parisrespire.mpp.data.http.AirparifAPI
import fr.parisrespire.mpp.data.http.model.Episode
import fr.parisrespire.mpp.data.http.model.Indice
import fr.parisrespire.mpp.data.http.model.IndiceJour
import fr.parisrespire.mpp.data.http.model.util.Day
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class AirQualityViewModel(private val uiExceptionHandler: UIExceptionHandler) : ViewModel() {

    private val _dayIndex = MutableLiveData<IndiceJour?>(null)
    val dayIndex: LiveData<IndiceJour?> = _dayIndex

    private val _indexList = MutableLiveData<Indice?>(null)
    val index: LiveData<Indice?> = _indexList

    private val _pollutionEpisode = MutableLiveData<Episode?>(null)
    val pollutionEpisode: LiveData<Episode?> = _pollutionEpisode

    private val airparifApi = AirparifAPI()

    fun fetchDayIndex(day: Day) {
        Logger.d("AirQualityViewModel", "fetchDayIndex()")
        viewModelScope.launch {
            try {
                withTimeout(10_000) {
                    val result = airparifApi.requestDayIndex(day)
                    _dayIndex.postValue(result)
                }
            } catch (exception: TimeoutCancellationException) {
                Logger.e("AirQualityViewModel", exception.toString())
                uiExceptionHandler.showError(CustomHttpRequestTimeoutException(exception.message, exception.cause))
            } catch (exception: CustomException) {
                Logger.e("AirQualityViewModel", exception.toString())
                uiExceptionHandler.showError(exception)
            }
        }
    }

    fun fetchIndex(day: Day) {
        viewModelScope.launch {
            try {
                withTimeout(10_000) {
                    val result = airparifApi.requestIndex()
                    _indexList.postValue(result.firstOrNull { it.date == day.value })
                }
            } catch (exception: TimeoutCancellationException) {
                Logger.e("AirQualityViewModel", exception.toString())
                uiExceptionHandler.showError(CustomHttpRequestTimeoutException(exception.message, exception.cause))
            } catch (exception: CustomException) {
                Logger.e("AirQualityViewModel", exception.toString())
                uiExceptionHandler.showError(exception)
            }
        }
    }

    fun fetchPollutionEpisode(day: Day) {
        viewModelScope.launch {
            try {
                withTimeout(10_000) {
                    val result = airparifApi.requestPollutionEpisode()
                    _pollutionEpisode.postValue(result.firstOrNull { it.date == day.value })
                }
            } catch (exception: TimeoutCancellationException) {
                Logger.e("AirQualityViewModel", exception.toString())
                uiExceptionHandler.showError(CustomHttpRequestTimeoutException(exception.message, exception.cause))
            } catch (exception: CustomException) {
                Logger.e("AirQualityViewModel", exception.toString())
                uiExceptionHandler.showError(exception)
            }
        }
    }
}
