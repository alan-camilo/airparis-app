package data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class O3(
    val indice: Int?,
    val url_carte: String?
)
