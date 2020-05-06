package fr.parisrespire.mpp.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class FacetGroup(
    val facets: List<Facet>,
    val name: String
)