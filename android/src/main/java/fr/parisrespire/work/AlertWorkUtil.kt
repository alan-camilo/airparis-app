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
package fr.parisrespire.work

import android.content.Context
import fr.parisrespire.R
import fr.parisrespire.mpp.data.http.model.Episode
import fr.parisrespire.mpp.data.http.model.PollutantDetails
import fr.parisrespire.mpp.data.http.model.util.PollutionLevel

class AlertWorkUtil(val context: Context) {

    fun formatAlertMessage(
        list: List<Pair<String, PollutantDetails>>,
        advice: String
    ): Pair<String, Boolean> {
        val filteredAlert = list.filter { it.second.niveau == PollutionLevel.ALERT.value }
        val filteredInfo = list.filter { it.second.niveau == PollutionLevel.INFO.value }
        var alertMessage: String? = null
        var infoMessage: String? = null
        val baseMessage = context.getString(R.string.pollution_message)
        if (filteredAlert.count() > 0) {
            val pollutantAlert =
                filteredAlert.joinToString(separator = ", ", transform = { it.first })
            alertMessage = String.format(
                baseMessage,
                context.getString(R.string.alert),
                pollutantAlert
            ).capitalize()
        }
        if (filteredInfo.count() > 0) {
            val pollutantInfo =
                filteredInfo.joinToString(separator = ", ", transform = { it.first })
            infoMessage = String.format(
                baseMessage,
                context.getString(R.string.information),
                pollutantInfo
            ).capitalize()
        }
        val listMessage = listOfNotNull(alertMessage, infoMessage, advice)
        val bigMessage = listMessage.count() > 1
        return Pair(listMessage.joinToString("<br>"), bigMessage)
    }

    fun episodeToPollutantList(episode: Episode): List<Pair<String, PollutantDetails>> {
        val list = listOf(
            Pair(context.getString(R.string.pm10), episode.pm10),
            Pair(context.getString(R.string.so2), episode.so2),
            Pair(context.getString(R.string.o3), episode.o3),
            Pair(context.getString(R.string.no2), episode.no2)
        )
        return list.filter { it.second != null } as List<Pair<String, PollutantDetails>>
    }
}
