package fr.parisrespire.mpp.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class Geometry(
    val coordinates: List<Double>,
    val type: String
)