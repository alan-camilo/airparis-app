package fr.parisrespire.mpp.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class PollutantIndex(
    val indice: Int?,
    val url_carte: String?
)
