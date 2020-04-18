package parisrespire.data.http

import com.github.florent37.log.Logger
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.withContext
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import parisrespire.base.API_KEY_PREFERENCE
import parisrespire.base.IO
import parisrespire.base.UserPreference
import parisrespire.data.ExceptionWrapper
import parisrespire.data.http.model.ApiKey

@UnstableDefault
class ApiKeyRepository(private val client: HttpClient) {

    constructor() : this(customHttpClient)

    suspend fun getApiKey(): String {
        var apiKey = UserPreference.getString(API_KEY_PREFERENCE, null)
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
