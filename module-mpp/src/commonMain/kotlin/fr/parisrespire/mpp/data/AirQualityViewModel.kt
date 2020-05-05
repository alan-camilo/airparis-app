package fr.parisrespire.mpp.data

import dev.icerock.moko.mvvm.viewmodel.ViewModel

class AirQualityViewModel(private val uiExceptionHandler: UIExceptionHandler) : ViewModel() {

    /*val mutex = Mutex()
    private var dataSetPollution = DataSetPollution()
    private val _dataSet = MutableLiveData<DataSetPollution?>(null)
    val dataSet: LiveData<DataSetPollution?> = _dataSet

    private val airparifApi = AirparifAPI()

    fun fetchDayIndex(day: Day) {
        Logger.d("AirQualityViewModel", "fetchDayIndex()")
        viewModelScope.launch {
            try {
                withTimeout(10_000) {
                    val result = airparifApi.requestDayIndex(day)
                    mutex.withLock {
                        dataSetPollution = dataSetPollution.copy(mDayIndex = result)
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
                Logger.e("AirQualityViewModel", exception.toString())
                uiExceptionHandler.showError(exception)
            }
        }
    }

    fun fetchIndex(day: Day) {
        viewModelScope.launch {
            try {
                withTimeout(10_000) {
                    val result = airparifApi.requestIndex().firstOrNull { it.date == day.value }
                    mutex.withLock {
                        dataSetPollution = dataSetPollution.copy(mIndex = result)
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
                        dataSetPollution = dataSetPollution.copy(mPollutionEpisode = result)
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
                Logger.e("AirQualityViewModel", exception.toString())
                uiExceptionHandler.showError(exception)
            }
        }
    }

    fun fetchIdxville(postalCode: String, day: Day) {
        viewModelScope.launch {
            try {
                withTimeout(10_000) {
                    val result = airparifApi.requestByCity(postalCode).firstOrNull()
                    mutex.withLock {
                        when (day) {
                            Day.YESTERDAY -> {
                                dataSetPollution = dataSetPollution.copy(mIdxvilleInfo = result?.hier)
                                _dataSet.postValue(dataSetPollution)
                            }
                            Day.TODAY -> {
                                dataSetPollution = dataSetPollution.copy(mIdxvilleInfo = result?.jour)
                                _dataSet.postValue(dataSetPollution)
                            }
                            Day.TOMORROW -> {
                                dataSetPollution = dataSetPollution.copy(mIdxvilleInfo = result?.demain)
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
                Logger.e("AirQualityViewModel", exception.toString())
                uiExceptionHandler.showError(exception)
            }
        }
    }*/
}
