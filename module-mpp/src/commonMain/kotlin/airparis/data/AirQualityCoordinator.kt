package airparis.data

import airparis.base.Coordinator
import airparis.data.http.model.util.Day

interface AirQualityCoordinator : Coordinator {
    fun showAirQuality(day: Day)
}
