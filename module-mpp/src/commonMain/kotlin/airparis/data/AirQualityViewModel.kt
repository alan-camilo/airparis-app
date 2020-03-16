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
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class AirQualityViewModel : BaseViewModel<AirQualityCoordinator, AirQualityState>(),
    AirQualityActions {

    private val airQualityRepo =
        AirQualityRepository(AirparifAPI())

    override fun getInitialState(): AirQualityState =
        AirQualityState(
            IndiceJour("", null, null, null, null),
            emptyList(),
            emptyList(),
            Day.TODAY
        )

    init {
        launch {
            airQualityRepo.subscribeDayIndex().consumeEach { dayIndex ->
                stateChannel.mutate { it.copy(dayIndex = dayIndex) }
            }
            airQualityRepo.subscribeIndex().consumeEach { list ->
                stateChannel.mutate { it.copy(indexList = list) }
            }
            airQualityRepo.subscribePollutionEpisode().consumeEach { list ->
                stateChannel.mutate { it.copy(pollutionEpisodeList = list) }
            }
        }
    }

    override fun fetchDayIndex(day: Day) {
        stateChannel.mutate { it.copy(day = day) }
        launch { airQualityRepo.fetchDayIndex(day) }
    }

    override fun fetchIndex() {
        stateChannel.mutate { it.copy() }
        launch { airQualityRepo.fetchIndex() }
    }

    override fun fetchPollutionEpisode() {
        stateChannel.mutate { it.copy() }
        launch { airQualityRepo.fetchPollutionEpisode() }
    }
}
