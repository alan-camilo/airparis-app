package fr.parisrespire.mpp.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class Commune(
    val code: String,
    val nom: String
)