package httpclient

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
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
    val result = Json.parse(
        Indice.serializer().list,
        client.get("https://www.airparif.asso.fr/services/api/1.1/indice")
    )
    client.close()
    return result.toString()
}