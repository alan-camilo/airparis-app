package http.model

import kotlinx.serialization.Serializable

@Serializable
data class Episode(
    val date: String,
    val detail: String?,
    val no2: No2Details? = null,
    val o3: O3Details? = null,
    val pm10: Pm10Details? = null,
    val so2: So2Details? = null
)
