package http.model

import kotlinx.serialization.Serializable

@Serializable
data class EpisodeO3(
    val criteres: List<String>,
    val niveau: String,
    val type: String
)
