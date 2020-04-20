package fr.parisrespire.mpp.data.http

interface ApiKeyRepository {
    suspend fun getApiKey(): String
}
