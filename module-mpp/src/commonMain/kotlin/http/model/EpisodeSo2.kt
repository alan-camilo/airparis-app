package http.model

import kotlinx.serialization.Serializable

@Serializable
data class EpisodeSo2(
    val criteres: List<String>,
    val niveau: String,
    val type: String
)
