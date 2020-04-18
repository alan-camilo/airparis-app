package parisrespire.data.http

import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ParametersBuilder
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.withContext
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import parisrespire.base.API_KEY_PREFERENCE
import parisrespire.base.IO
import parisrespire.base.UserPreference
import parisrespire.data.ExceptionWrapper
import parisrespire.data.http.model.Episode
import parisrespire.data.http.model.Indice
import parisrespire.data.http.model.IndiceJour
import parisrespire.data.http.model.util.Day

@UnstableDefault
class AirparifAPI(private val client: HttpClient) {

    constructor() : this(customHttpClient)

    private val apiKeyRepository = ApiKeyRepository()

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
            val mBody = getBodyParam()
            return withContext(IO) {
                val response = client.post<String> {
                    url(urlBuilder.buildString())
                    body = mBody
                }
                Json.parse(IndiceJour.serializer(), response)
            }
        } catch (throwable: Throwable) {
            handleWrongApiKey(throwable)
            throw ExceptionWrapper(throwable).getCustomException()
        }
    }

    suspend fun requestPollutionEpisode(): List<Episode> {
        try {
            val mBody = getBodyParam()
            return withContext(IO) {
                val response: String = client.post {
                    url(URL_EPISODE_POLLUTION)
                    body = mBody
                }
                Json.parse(Episode.serializer().list, response)
            }
        } catch (throwable: Throwable) {
            handleWrongApiKey(throwable)
            throw ExceptionWrapper(throwable).getCustomException()
        }
    }

    suspend fun requestIndex(): List<Indice> {
        try {
            val mBody = getBodyParam()
            return withContext(IO) {
                val response: String = client.post {
                    url(URL_INDICE)
                    body = mBody
                }
                Json.parse(Indice.serializer().list, response)
            }
        } catch (throwable: Throwable) {
            handleWrongApiKey(throwable)
            throw ExceptionWrapper(throwable).getCustomException()
        }
    }

    private suspend fun handleWrongApiKey(throwable: Throwable) {
        if (throwable is ClientRequestException && throwable.response.status.value == 403) {
            val channel = throwable.response.content
            var str = ""
            while (channel.availableForRead > 0) {
                str += channel.readUTF8Line()
            }
            if (str == "{\"erreur\":\"Cl\\u00e9 invalide\"}") {
                UserPreference.set(API_KEY_PREFERENCE, null)
            }
        }
    }

    private suspend fun getBodyParam(): MultiPartFormDataContent {
        val apiKey = apiKeyRepository.getApiKey()
        return MultiPartFormDataContent(
            formData {
                append("key", apiKey)
            }
        )
    }
}
