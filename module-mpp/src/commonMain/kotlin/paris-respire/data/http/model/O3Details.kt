package paris-respire.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class O3Details(
    val criteres: List<String>,
    val niveau: String,
    val type: String
)
