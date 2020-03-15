package airparis.data

import airparis.base.State
import airparis.data.http.model.Episode
import airparis.data.http.model.Indice
import airparis.data.http.model.IndiceJour
import airparis.data.http.model.util.Day

data class AirQualityState(
    val dayIndex: IndiceJour,
    val indexList: List<Indice>,
    val pollutionEpisodeList: List<Episode>,
    val day: Day
) : State
