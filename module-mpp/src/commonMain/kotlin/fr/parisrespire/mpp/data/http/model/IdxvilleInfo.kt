package fr.parisrespire.mpp.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class IdxvilleInfo(
    val indice: Int,
    val polluants: List<String>
)