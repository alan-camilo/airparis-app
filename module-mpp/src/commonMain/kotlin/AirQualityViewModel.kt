import http.AirparifAPI
import http.model.Episode
import http.model.Indice
import http.model.IndiceJour
import http.util.Day
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class AirQualityViewModel : BaseViewModel<AirQualityCoordinator, AirQualityState>(),
    AirQualityActions {

    private val airQualityRepo = AirQualityRepository(AirparifAPI())

    override fun getInitialState(): AirQualityState =
        AirQualityState(IndiceJour("", null, null, null, null), emptyList(), emptyList(), Day.TODAY)

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

data class AirQualityState(
    val dayIndex: IndiceJour,
    val indexList: List<Indice>,
    val pollutionEpisodeList: List<Episode>,
    val day: Day
) : State

interface AirQualityActions : Actions {
    fun fetchDayIndex(day: Day)
    fun fetchIndex()
    fun fetchPollutionEpisode()
}

interface AirQualityCoordinator : Coordinator {
    fun showAirQuality(day: Day)
}
