/*
This file is part of airparis.

airparis is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or any
later version.

airparis is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with airparis.  If not, see <https://www.gnu.org/licenses/>.
*/
package airparis.data

import airparis.base.BaseViewModel
import airparis.data.http.AirparifAPI
import airparis.data.http.model.IndiceJour
import airparis.data.http.model.util.Day
import airparis.data.http.model.util.toDay
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class AirQualityViewModel : BaseViewModel<AirQualityCoordinator, AirQualityState>(),
    AirQualityActions {

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
        //stateChannel.mutate { it.copy() }
        launch { airQualityRepo.fetchDayIndex(day) }
    }

    override fun fetchIndex() {
        //stateChannel.mutate { it.copy() }
        launch { airQualityRepo.fetchIndex() }
    }

    override fun fetchPollutionEpisode() {
        //stateChannel.mutate { it.copy() }
        launch { airQualityRepo.fetchPollutionEpisode() }
    }
}
