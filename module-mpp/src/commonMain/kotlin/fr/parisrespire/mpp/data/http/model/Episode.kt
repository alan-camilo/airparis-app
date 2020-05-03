package fr.parisrespire.mpp.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class Episode(
    val date: String,
    val detail: String,
    val no2: PollutantDetails? = null,
    val o3: PollutantDetails? = null,
    val pm10: PollutantDetails? = null,
    val so2: PollutantDetails? = null
) {
    fun isEmpty() = detail.isEmpty() && no2 == null && o3 == null && pm10 == null && so2 == null

    fun isNotEmpty() = !isEmpty()
}
