package http

import http.model.Episode
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.url
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json

@UnstableDefault
val client = HttpClient() {
    install(JsonFeature) {
        serializer = KotlinxSerializer(Json.nonstrict)
    }
}

@UnstableDefault
suspend fun request(): String {
    // val result = client.get<IndiceJour>("https://www.airparif.asso.fr/services/api/1.1/indiceJour?date=jour")
    val post: String = client.post() {
        url("https://www.airparif.asso.fr/services/api/1.1/episode")
        body = MultiPartFormDataContent(
            formData {
                append("key", "003f3ca2-f3c3-1e0e-7734-c344758580a1")
            }
        )
    }
    val result = Json.parse(Episode.serializer().list, post)
    client.close()
    return "${result[0].date} ${result[0].detail}"
    // return "${result.date} ${result.detail}"
    // return result.toString()
}
