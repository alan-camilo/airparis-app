package parisrespire.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val erreur: String
)
