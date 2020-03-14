package airparis.data

import airparis.base.Actions
import airparis.data.http.model.util.Day

interface AirQualityActions : Actions {
    fun fetchDayIndex(day: Day)
    fun fetchIndex()
    fun fetchPollutionEpisode()
}