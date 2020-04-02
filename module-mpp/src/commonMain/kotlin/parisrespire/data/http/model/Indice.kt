package parisrespire.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class Indice(
    val date: String,
    val indice: Int?,
    val url_carte: String?
) {
    override fun toString(): String = "$date $indice $url_carte"
}
