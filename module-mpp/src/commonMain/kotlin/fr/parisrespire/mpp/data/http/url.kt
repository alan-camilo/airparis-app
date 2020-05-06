package fr.parisrespire.mpp.data.http

import io.ktor.http.Url

const val HOST_AIRPARIF = "airparif.asso.fr"
const val HOST_GEO_API = "datanova.legroupe.laposte.fr"
const val PATH_INDICE_JOUR = "services/api/1.1/indiceJour"
const val PATH_INDICE = "services/api/1.1/indice"
const val PATH_EPISODE_POLLUTION = "services/api/1.1/episode"
const val PATH_IDXVILLE = "services/api/1.1/idxville"
const val PATH_SEARCH = "api/records/1.0/search"
val URL_INDICE_JOUR = Url("https://www.$HOST_AIRPARIF/$PATH_INDICE_JOUR")
val URL_EPISODE_POLLUTION = Url("https://www.$HOST_AIRPARIF/$PATH_EPISODE_POLLUTION")
val URL_INDICE = Url("https://www.$HOST_AIRPARIF/$PATH_INDICE")
val URL_IDXVILLE = Url("https://www.$HOST_AIRPARIF/$PATH_IDXVILLE")
val URL_API_KEY = Url("https://paris-respire.s3.eu-west-3.amazonaws.com/airparif-api-key.json")
val URL_GEO_API = Url("https://$HOST_GEO_API/$PATH_SEARCH")
