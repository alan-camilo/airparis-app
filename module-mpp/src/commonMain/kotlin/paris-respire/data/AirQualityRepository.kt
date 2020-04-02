/*
This file is part of paris-respire.

paris-respire is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or any
later version.

paris-respire is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with paris-respire.  If not, see <https://www.gnu.org/licenses/>.
*/
package paris-respire.data

import paris-respire.data.http.AirparifAPI
import paris-respire.data.http.model.Episode
import paris-respire.data.http.model.Indice
import paris-respire.data.http.model.IndiceJour
import paris-respire.data.http.model.util.Day
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
