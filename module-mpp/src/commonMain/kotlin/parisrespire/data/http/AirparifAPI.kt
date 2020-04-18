package parisrespire.data.http

import io.ktor.client.HttpClient
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
import parisrespire.base.IO
import parisrespire.data.ExceptionWrapper
import parisrespire.data.http.model.Episode
import parisrespire.data.http.model.Indice
import parisrespire.data.http.model.IndiceJour
import parisrespire.data.http.model.util.Day

@UnstableDefault
class AirparifAPI(private val client: HttpClient) {

    constructor() : this(customHttpClient)

    private val body = MultiPartFormDataContent(
        formData {
            append("key", API_KEY)
        }
    )

    suspend fun requestDayIndex(day: Day): IndiceJour {
        val argument = ParametersBuilder().apply {
            append("date", day.value)
        }
        val urlBuilder = URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = HOST,
            encodedPath = PATH_INDICE_JOUR,
            parameters = argument
        )
        try {
            return withContext(IO) {
                val response = client.post<String> {
                    url(urlBuilder.buildString())
                    body = this@AirparifAPI.body
                }
                Json.parse(IndiceJour.serializer(), response)
            }
        } catch (throwable: Throwable) {
            throw ExceptionWrapper(throwable).getCustomException()
        }
    }

    suspend fun requestPollutionEpisode(): List<Episode> {
        try {
            return withContext(IO) {
                val response: String = client.post {
                    url(URL_EPISODE_POLLUTION)
                    body = this@AirparifAPI.body
                }
                Json.parse(Episode.serializer().list, response)
            }
        } catch (throwable: Throwable) {
            throw ExceptionWrapper(throwable).getCustomException()
        }
    }

    suspend fun requestIndex(): List<Indice> {
        try {
            return withContext(IO) {
                val response: String = client.post {
                    url(URL_INDICE)
                    body = this@AirparifAPI.body
                }
                Json.parse(Indice.serializer().list, response)
            }
        } catch (throwable: Throwable) {
            throw ExceptionWrapper(throwable).getCustomException()
        }
    }
}
