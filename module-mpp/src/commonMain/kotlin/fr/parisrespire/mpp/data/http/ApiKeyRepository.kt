package fr.parisrespire.mpp.data.http

import com.github.florent37.log.Logger
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.withContext
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import fr.parisrespire.mpp.base.API_KEY_PREFERENCE
import fr.parisrespire.mpp.base.IO
import fr.parisrespire.mpp.base.UserPreference
import fr.parisrespire.mpp.data.ExceptionWrapper
import fr.parisrespire.mpp.data.http.model.ApiKey

@UnstableDefault
class ApiKeyRepository(private val client: HttpClient) {

    constructor() : this(customHttpClient)

    suspend fun getApiKey(): String {
        var apiKey = UserPreference.getString(
            API_KEY_PREFERENCE, null)
        Logger.e("ApiKeyRepository", "UserPreference apiKey=$apiKey")
        if (apiKey == null) {
            apiKey = requestHttpApiKey()
            UserPreference.set(API_KEY_PREFERENCE, apiKey)
        }
        return apiKey
    }

    private suspend fun requestHttpApiKey(): String {
        try {
            return withContext(IO) {
                val response = client.get<String>(url = URL_API_KEY)
                val obj = Json.parse(ApiKey.serializer(), response)
                obj.apiKey
            }
        } catch (throwable: Throwable) {
            throw ExceptionWrapper(throwable).getCustomException()
        }
    }
}
