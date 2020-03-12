package http

import dispatchers.IO
import http.model.Episode
import http.model.Indice
import http.model.IndiceJour
import http.util.Jour
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ParametersBuilder
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import kotlinx.coroutines.withContext
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json

@UnstableDefault
object Client {
    private val client = HttpClient() {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json.nonstrict)
        }
    }

    private val body = MultiPartFormDataContent(
        formData {
            append("key", API_KEY)
        }
    )

    suspend fun requestIndiceJour(day: Jour): IndiceJour {
        val argument = ParametersBuilder().apply {
            append("date", day.value)
        }
        val urlBuilder = URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = HOST,
            encodedPath = PATH_INDICE_JOUR,
            parameters = argument
        )
        return withContext(IO) {
            client.post<IndiceJour> {
                url(urlBuilder.buildString())
                body = this@Client.body
            }
        }
    }

    suspend fun requestEpisodePollution(): List<Episode> {
        return withContext(IO) {
            val response: String = client.post {
                url(URL_EPISODE_POLLUTION.toString()) {
                    body = this@Client.body
                }
            }
            Json.parse(Episode.serializer().list, response)
        }
    }

    suspend fun requestIndice(): List<Indice> {
        return withContext(IO) {
            val response: String = client.post {
                url(URL_INDICE.toString()) {
                    body = this@Client.body
                }
            }
            Json.parse(Indice.serializer().list, response)
        }
    }
}
