package fr.parisrespire.mpp.data.http.model

import kotlinx.serialization.Serializable

@Serializable
data class Fields(
    val code_commune_insee: String,
    val code_postal: String,
    val coordonnees_gps: List<Double>,
    val libelle_d_acheminement: String,
    val nom_de_la_commune: String
)