/*
This file is part of Paris respire.

Paris respire is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or any
later version.

Paris respire is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Paris respire.  If not, see <https://www.gnu.org/licenses/>.
*/
package parisrespire.data

import com.github.florent37.log.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import parisrespire.base.BaseViewModel
import parisrespire.data.http.AirparifAPI
import parisrespire.data.http.model.IndiceJour
import parisrespire.data.http.model.util.Day
import parisrespire.data.http.model.util.toDay

class AirQualityViewModel(private val uiExceptionHandler: UIExceptionHandler) :
    BaseViewModel<AirQualityState>(),
    AirQualityActions {

    private val errorHandler = CoroutineExceptionHandler { _, error ->
        when (error) {
            is CustomException -> {
                Logger.e("AirQualityViewModel", error.throwable.toString())
                uiExceptionHandler.showError(error)
            }
            else -> throw error
        }
    }

    private val airQualityRepo =
        AirQualityRepository(AirparifAPI())

    override fun getInitialState(): AirQualityState =
        AirQualityState(
            hashMapOf(
                Day.YESTERDAY to IndiceJour("", null, null, null, null),
                Day.TODAY to IndiceJour("", null, null, null, null),
                Day.TOMORROW to IndiceJour("", null, null, null, null)
            ),
            emptyList(),
            emptyList()
        )

    init {
        launch {
            airQualityRepo.subscribeDayIndex().consumeEach { dayIndex ->
                stateChannel.mutate { airQualityState ->
                    dayIndex.date.toDay()?.let {
                        airQualityState.dayIndexMap[it] = dayIndex
                    }
                    airQualityState.copy()
                }
            }
        }
        launch {
            airQualityRepo.subscribeIndex().consumeEach { list ->
                stateChannel.mutate { airQualityState -> airQualityState.copy(indexList = list) }
            }
        }
        launch {
            airQualityRepo.subscribePollutionEpisode().consumeEach { list ->
                stateChannel.mutate { airQualityState ->
                    airQualityState.copy(
                        pollutionEpisodeList = list
                    )
                }
            }
        }
    }

    override fun fetchDayIndex(day: Day) {
        launch(errorHandler) {
            airQualityRepo.fetchDayIndex(day)
        }
    }

    override fun fetchIndex() {
        launch(errorHandler) { airQualityRepo.fetchIndex() }
    }

    override fun fetchPollutionEpisode() {
        launch(errorHandler) {
            airQualityRepo.fetchPollutionEpisode()
        }
    }
}
