package fr.parisrespire.mpp.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class Idxville(
    val hier: IdxvilleInfo?,
    val jour: IdxvilleInfo?,
    val demain: IdxvilleInfo? = null,
    val ninsee: String?
)