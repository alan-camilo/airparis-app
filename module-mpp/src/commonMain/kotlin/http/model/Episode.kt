package http.model

import kotlinx.serialization.Serializable

@Serializable
data class Episode(
    val date: String,
    val detail: String?,
    val no2: EpisodeNo2? = null,
    val o3: EpisodeO3? = null,
    val pm10: EpisodePm10? = null,
    val so2: EpisodeSo2? = null
)