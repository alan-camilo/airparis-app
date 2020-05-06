package fr.parisrespire.mpp.data

import com.github.florent37.log.Logger
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.parisrespire.mpp.base.*
import fr.parisrespire.mpp.data.http.AirparifAPI
import fr.parisrespire.mpp.data.http.NetworkConnectivityImpl
import fr.parisrespire.mpp.data.http.model.util.DataSetPollution
import fr.parisrespire.mpp.data.http.model.util.Day
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeout

class AirQualityViewModel(private val uiExceptionHandler: UIExceptionHandler) : ViewModel() {

    private val mutex = Mutex()
    private var dataSetPollution = DataSetPollution()
    private val _dataSet = MutableLiveData<DataSetPollution?>(null)
    val dataSet: LiveData<DataSetPollution?> = _dataSet
    private var _inseeCode: MutableLiveData<String>
    var inseeCode: LiveData<String>
    private var _cityName: MutableLiveData<String>
    var cityName: LiveData<String>

    private val airparifApi = AirparifAPI()

    init {
        val code = UserPreference.getString(INSEE_CODE_PREFERENCE, defaultInseeCode)!!
        _inseeCode = MutableLiveData(code)
        inseeCode = _inseeCode
        val name = UserPreference.getString(CITY_NAME_PREFERENCE, defaultCityName)!!
        _cityName = MutableLiveData(name)
        cityName = _cityName
    }

    fun checkNewLocation(day: Day) {
        val code = UserPreference.getString(INSEE_CODE_PREFERENCE, defaultInseeCode)!!
        try {
            if (code != inseeCode.value && NetworkConnectivityImpl.checkConnectivity()) {
                fetchIdxville(code, day)
                _inseeCode.postValue(code)
                val name = UserPreference.getString(CITY_NAME_PREFERENCE, defaultCityName)!!
                _cityName.postValue(name)
            }
        } catch (exception: CustomException) {
            Logger.e("AirQualityViewModel", "exception=$exception")
            uiExceptionHandler.showError(exception)
        }
    }

    fun fetchData(day: Day) {
        Logger.d("AirQualityViewModel", "fetchData")
        fetchDayIndex(day)
        fetchIdxville(inseeCode.value, day)
        fetchPollutionEpisode(day)
    }

    fun fetchDayIndex(day: Day) {
        Logger.d("AirQualityViewModel", "fetchDayIndex()")
        viewModelScope.launch {
            try {
                withTimeout(10_000) {
                    val result = airparifApi.requestDayIndex(day)
                    mutex.withLock {
                        dataSetPollution = dataSetPollution.copy(dayIndex = result)
                        _dataSet.postValue(dataSetPollution)
                    }
                }
            } catch (exception: TimeoutCancellationException) {
                Logger.e("AirQualityViewModel", "timeout $exception")
                uiExceptionHandler.showError(
                    CustomHttpRequestTimeoutException(
                        exception.message,
                        exception.cause
                    )
                )
            } catch (exception: CustomException) {
                Logger.e("AirQualityViewModel", "fetchDayIndex ${day.value} exception=$exception")
                uiExceptionHandler.showError(exception)
            }
        }
    }

    private fun fetchIndex(day: Day) {
        viewModelScope.launch {
            try {
                withTimeout(10_000) {
                    val result = airparifApi.requestIndex().firstOrNull { it.date == day.value }
                    mutex.withLock {
                        dataSetPollution = dataSetPollution.copy(index = result)
                        _dataSet.postValue(dataSetPollution)
                    }
                }
            } catch (exception: TimeoutCancellationException) {
                Logger.e("AirQualityViewModel", exception.toString())
                uiExceptionHandler.showError(
                    CustomHttpRequestTimeoutException(
                        exception.message,
                        exception.cause
                    )
                )
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
                    val result =
                        airparifApi.requestPollutionEpisode().firstOrNull { it.date == day.value }
                    mutex.withLock {
                        dataSetPollution = dataSetPollution.copy(pollutionEpisode = result)
                        _dataSet.postValue(dataSetPollution)
                    }
                }
            } catch (exception: TimeoutCancellationException) {
                Logger.e("AirQualityViewModel", "timeout $exception")
                uiExceptionHandler.showError(
                    CustomHttpRequestTimeoutException(
                        exception.message,
                        exception.cause
                    )
                )
            } catch (exception: CustomException) {
                Logger.e(
                    "AirQualityViewModel",
                    "fetchPollutionEpisode ${day.value} exception=$exception"
                )
                uiExceptionHandler.showError(exception)
            }
        }
    }

    fun fetchIdxville(inseeCode: String, day: Day) {
        Logger.d("AirQualityViewModel", "fetchIdxville inseeCode=$inseeCode")
        viewModelScope.launch {
            try {
                withTimeout(10_000) {
                    val result = airparifApi.requestByCity(inseeCode).firstOrNull()
                    Logger.d("AirQualityViewModel", "fetchIdxville result=$result")
                    mutex.withLock {
                        when (day) {
                            Day.YESTERDAY -> {
                                dataSetPollution =
                                    dataSetPollution.copy(idxvilleInfo = result?.hier)
                                _dataSet.postValue(dataSetPollution)
                            }
                            Day.TODAY -> {
                                dataSetPollution =
                                    dataSetPollution.copy(idxvilleInfo = result?.jour)
                                _dataSet.postValue(dataSetPollution)
                            }
                            Day.TOMORROW -> {
                                dataSetPollution =
                                    dataSetPollution.copy(idxvilleInfo = result?.demain)
                                _dataSet.postValue(dataSetPollution)
                            }
                        }
                    }
                }
            } catch (exception: TimeoutCancellationException) {
                Logger.e("AirQualityViewModel", "timeout $exception")
                uiExceptionHandler.showError(
                    CustomHttpRequestTimeoutException(
                        exception.message,
                        exception.cause
                    )
                )
            } catch (exception: CustomException) {
                Logger.e("AirQualityViewModel", "fetchIdxville ${day.value} exception=$exception")
                uiExceptionHandler.showError(exception)
            }
        }
    }
}
