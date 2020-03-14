package airparis.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class IndiceJour(
    val date: String,
    val global: Global?,
    val no2: No2?,
    val o3: O3?,
    val pm10: Pm10?
)
