package airparis.data

import airparis.base.Actions
import airparis.base.BaseViewModel
import airparis.base.Coordinator
import airparis.base.State
import airparis.data.http.AirparifAPI
import airparis.data.http.model.Episode
import airparis.data.http.model.Indice
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
