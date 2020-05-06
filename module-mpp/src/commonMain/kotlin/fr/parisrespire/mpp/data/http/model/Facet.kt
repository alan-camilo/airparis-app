package fr.parisrespire.mpp.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class Facet(
    val count: Int,
    val name: String,
    val path: String,
    val state: String
)