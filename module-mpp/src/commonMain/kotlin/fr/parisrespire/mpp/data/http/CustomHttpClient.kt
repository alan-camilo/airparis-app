package fr.parisrespire.mpp.data.http

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json

val customHttpClient = HttpClient() {
    install(JsonFeature) {
        serializer = KotlinxSerializer(Json.nonstrict)
    }
}
