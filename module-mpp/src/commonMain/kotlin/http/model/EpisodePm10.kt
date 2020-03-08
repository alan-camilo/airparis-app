package http.model

import kotlinx.serialization.Serializable

@Serializable
data class EpisodePm10(
    val criteres: List<String>,
    val niveau: String,
    val type: String
)