package parisrespire.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class Episode(
    val date: String,
    val detail: String?,
    val no2: PollutantDetails? = null,
    val o3: PollutantDetails? = null,
    val pm10: PollutantDetails? = null,
    val so2: PollutantDetails? = null
)
