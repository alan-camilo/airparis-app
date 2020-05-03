package fr.parisrespire.mpp.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class IndiceJour(
    val date: String,
    val global: PollutantIndex? = null,
    val no2: PollutantIndex? = null,
    val o3: PollutantIndex? = null,
    val pm10: PollutantIndex? = null,
    // The two properties below are not in the documentation but are real! Do not delete
    val url_carte: String? = null,
    val indices: String? = null
)
