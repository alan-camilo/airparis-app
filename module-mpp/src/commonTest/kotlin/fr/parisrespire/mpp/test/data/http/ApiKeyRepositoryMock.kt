package fr.parisrespire.mpp.test.data.http

import fr.parisrespire.mpp.data.http.ApiKeyRepository

class ApiKeyRepositoryMock : ApiKeyRepository {
    override suspend fun getApiKey(): String {
        return "dumb mock"
    }
}
