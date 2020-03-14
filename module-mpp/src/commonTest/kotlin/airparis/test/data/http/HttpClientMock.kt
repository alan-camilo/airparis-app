package airparis.test.data.http

import airparis.data.http.URL_EPISODE_POLLUTION
import airparis.data.http.URL_INDICE
import airparis.data.http.URL_INDICE_JOUR
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.engine.mock.respondOk
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.hostWithPort

val mockResponseOK = HttpClient(MockEngine) {
    engine {
        addHandler { request ->
            when (request.url.encodedPath) {
                URL_INDICE.encodedPath -> {
                    respondOk(jsonIndice)
                }
                URL_INDICE_JOUR.encodedPath -> {
                    respondOk(jsonIndiceJour)
                }
                URL_EPISODE_POLLUTION.encodedPath -> {
                    respondOk(jsonEpisodePollution)
                }
                else -> error("Unhandled ${request.url.fullUrl} ${request.method}")
            }
        }
    }
}

val mockResponseBadJson = HttpClient(MockEngine) {
    engine {
        addHandler { request ->
            when (request.url.encodedPath) {
                URL_INDICE.encodedPath -> {
                    respondOk(jsonIndiceJour)
                }
                URL_INDICE_JOUR.encodedPath -> {
                    respondOk(jsonEpisodePollution)
                }
                URL_EPISODE_POLLUTION.encodedPath -> {
                    respondOk(jsonIndice)
                }
                else -> error("Unhandled ${request.url.fullUrl} ${request.method}")
            }
        }
    }
}

val mockResponseBadRequest = HttpClient(MockEngine) {
    engine {
        addHandler {
            respondBadRequest()
        }
    }
}

private val Url.hostWithPortIfRequired: String get() = if (port == protocol.defaultPort) host else hostWithPort

private val Url.fullUrl: String get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"

const val jsonIndice = "[\n" +
        "    {\n" +
        "        \"date\": \"hier\",\n" +
        "        \"url_carte\": \"https://www.airparif.asso.fr/services/cartes/indice/date/hier\",\n" +
        "        \"indice\": 31 \n" +
        "    },\n" +
        "    {\n" +
        "        \"date\": \"jour\",\n" +
        "        \"url_carte\": \"https://www.airparif.asso.fr/services/cartes/indice/date/jour\",\n" +
        "        \"indice\": null\n" +
        "    },\n" +
        "    {\n" +
        "        \"date\": \"demain\",\n" +
        "        \"url_carte\": \"https://www.airparif.asso.fr/services/cartes/indice/date/demain\",\n" +
        "        \"indice\": 40\n" +
        "    }\n" +
        "]"

const val jsonIndiceJour = "{\n" +
        "    \"date\": \"13/03/2020\",\n" +
        "    \"global\": {\n" +
        "        \"indice\": 40,\n" +
        "        \"url_carte\": \"https://www.airparif.asso.fr/services/cartes/indice/date/jour\"\n" +
        "    },\n" +
        "    \"no2\": {\n" +
        "        \"indice\": 37,\n" +
        "        \"url_carte\": \"https://www.airparif.asso.fr/services/cartes/indice/date/jour/pol/NO2\"\n" +
        "    },\n" +
        "    \"o3\": {\n" +
        "        \"indice\": 40,\n" +
        "        \"url_carte\": \"https://www.airparif.asso.fr/services/cartes/indice/date/jour/pol/O3\"\n" +
        "    },\n" +
        "    \"pm10\": {\n" +
        "        \"indice\": 31,\n" +
        "        \"url_carte\": \"\"\n" +
        "    }\n" +
        "}"

const val jsonEpisodePollution = "[\n" +
        "    {\n" +
        "        \"date\": \"hier\",\n" +
        "        \"detail\": \"\"\n" +
        "    },\n" +
        "    {\n" +
        "        \"date\": \"jour\",\n" +
        "        \"detail\": \"\"\n" +
        "    },\n" +
        "    {\n" +
        "        \"date\": \"demain\",\n" +
        "        \"detail\": \"\"\n" +
        "    }\n" +
        "]"
