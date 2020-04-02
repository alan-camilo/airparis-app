package parisrespire.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class IndiceJour(
    val date: String,
    val global: Global? = null,
    val no2: No2? = null,
    val o3: O3? = null,
    val pm10: Pm10? = null,
    val url_carte: String? = null,
    val indices: String? = null
) {
    fun isEmpty() =
        date.isEmpty() && global == null && no2 == null && o3 == null && pm10 == null && url_carte.isNullOrEmpty() && indices.isNullOrEmpty()

    fun isNotEmpty() =
        !isEmpty()
}
