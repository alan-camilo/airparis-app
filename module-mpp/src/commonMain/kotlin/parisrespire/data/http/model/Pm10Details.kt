package parisrespire.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class Pm10Details(
    val criteres: List<String>,
    val niveau: String,
    val type: String
)
