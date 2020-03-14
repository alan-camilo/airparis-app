package http.model

import kotlinx.serialization.Serializable

@Serializable
data class No2Details(
    val criteres: List<String>,
    val niveau: String,
    val type: String
)
