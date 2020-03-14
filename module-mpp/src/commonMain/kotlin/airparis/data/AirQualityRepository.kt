package data

import data.http.AirparifAPI
import data.http.model.Episode
import data.http.model.Indice
import data.http.model.IndiceJour
import data.http.model.util.Day
import kotlinx.coroutines.channels.ConflatedBroadcastChannel

class AirQualityRepository(private val airparifAPI: AirparifAPI) {
    private val dayIndexChannel = ConflatedBroadcastChannel<IndiceJour>()
    private val indexChannel = ConflatedBroadcastChannel<List<Indice>>()
    private val pollutionEpisodeChannel = ConflatedBroadcastChannel<List<Episode>>()

    fun subscribeDayIndex() = dayIndexChannel.openSubscription()
    fun subscribeIndex() = indexChannel.openSubscription()
    fun subscribePollutionEpisode() = pollutionEpisodeChannel.openSubscription()

    suspend fun fetchDayIndex(day: Day) {
        airparifAPI.requestDayIndex(day).also {
            dayIndexChannel.offer(it)
        }
    }

    suspend fun fetchIndex() {
        airparifAPI.requestIndex().also {
            indexChannel.offer(it)
        }
    }

    suspend fun fetchPollutionEpisode() {
        airparifAPI.requestPollutionEpisode().also {
            pollutionEpisodeChannel.offer(it)
        }
    }
}
