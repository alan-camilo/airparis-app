package paris-respire.data.http

import io.ktor.http.Url

const val API_KEY = "003f3ca2-f3c3-1e0e-7734-c344758580a1"
const val HOST = "airparif.asso.fr"
const val PATH_INDICE_JOUR = "services/api/1.1/indiceJour"
const val PATH_INDICE = "services/api/1.1/indice"
const val PATH_EPISODE_POLLUTION = "services/api/1.1/episode"
val URL_INDICE_JOUR = Url("https://www.$HOST/$PATH_INDICE_JOUR")
val URL_EPISODE_POLLUTION = Url("https://www.$HOST/$PATH_EPISODE_POLLUTION")
val URL_INDICE = Url("https://www.$HOST/$PATH_INDICE")
