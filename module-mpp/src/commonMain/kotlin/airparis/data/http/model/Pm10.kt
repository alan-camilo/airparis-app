package data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class Pm10(
    val indice: Int?,
    val url_carte: String?
)
