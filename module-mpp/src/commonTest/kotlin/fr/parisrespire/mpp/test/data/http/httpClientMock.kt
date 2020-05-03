/*
This file is part of Paris respire.

Paris respire is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or any
later version.

Paris respire is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Paris respire.  If not, see <https://www.gnu.org/licenses/>.
*/
package fr.parisrespire.mpp.test.data.http

import fr.parisrespire.mpp.data.http.URL_EPISODE_POLLUTION
import fr.parisrespire.mpp.data.http.URL_IDXVILLE
import fr.parisrespire.mpp.data.http.URL_INDICE
import fr.parisrespire.mpp.data.http.URL_INDICE_JOUR
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
                URL_IDXVILLE.encodedPath -> {
                    respondOk(jsonIdxville)
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
                    respondOk(jsonIdxville)
                }
                URL_INDICE_JOUR.encodedPath -> {
                    respondOk(jsonEpisodePollution)
                }
                URL_EPISODE_POLLUTION.encodedPath -> {
                    respondOk(jsonIndice)
                }
                URL_IDXVILLE.encodedPath -> {
                    respondOk(jsonIndiceJour)
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

const val jsonEpisodePollution = "[ \n" +
        "    { \n" +
        "        \"date\" : \"hier\", \n" +
        "        \"o3\" : {\n" +
        "            \"type\" : \"constate\",\n" +
        "            \"niveau\" : \"info\", \n" +
        "            \"criteres\": [\"km\", \"pop\"] \n" +
        "        }, \n" +
        "        \"so2\" : {\n" +
        "            \"type\" : \"constate\", \n" +
        "            \"niveau\" : \"alerte\", \n" +
        "            \"criteres\": [\"pop\"] \n" +
        "        },\n" +
        "        \"detail\": \"\" \n" +
        "    }, \n" +
        "    { \n" +
        "        \"date\" : \"jour\", \n" +
        "        \"detail\": \"\" \n" +
        "    },\n" +
        "    { \n" +
        "        \"date\" : \"demain\",\n" +
        "        \"no2\" : {\n" +
        "            \"type\" : \"constate\",\n" +
        "            \"niveau\" : \"info\",\n" +
        "            \"criteres\": [\"km\"] \n" +
        "        }, \n" +
        "        \"so2\" : { \n" +
        "            \"type\" : \"constate\", \n" +
        "            \"niveau\" : \"alerte\", \n" +
        "            \"criteres\": [\"km\"] \n" +
        "        },\n" +
        "        \"detail\": \"Il est conseillé d'éviter les déplacements en Ile de France\" \n" +
        "    }\n" +
        "]"

const val jsonIdxville = "[\n" +
        "    {\n" +
        "        \"ninsee\": \"91228\",\n" +
        "        \"hier\": {\n" +
        "            \"indice\": 38,\n" +
        "            \"polluants\": [\n" +
        "                \"o3\"\n" +
        "            ]\n" +
        "        },\n" +
        "        \"jour\": {\n" +
        "            \"indice\": 35,\n" +
        "            \"polluants\": [\n" +
        "                \"o3\"\n" +
        "            ]\n" +
        "        },\n" +
        "        \"demain\": {\n" +
        "            \"indice\": 40,\n" +
        "            \"polluants\": [\n" +
        "                \"o3\"\n" +
        "            ]\n" +
        "        }\n" +
        "    }\n" +
        "]"
