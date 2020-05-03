package fr.parisrespire.mpp.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class Idxville(
    val demain: IdxvilleInfo?,
    val hier: IdxvilleInfo?,
    val jour: IdxvilleInfo?,
    val ninsee: String?
)