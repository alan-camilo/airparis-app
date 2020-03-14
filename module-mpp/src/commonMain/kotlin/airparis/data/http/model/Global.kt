package airparis.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class Global(
    val indice: Int?,
    val url_carte: String?
)
