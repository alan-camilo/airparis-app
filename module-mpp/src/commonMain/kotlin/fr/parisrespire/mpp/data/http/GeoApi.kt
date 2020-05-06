/*
This file is part of Paris respire.

Paris respire is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or any 
later version.

Paris respire is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Paris respire.  If not, see <https://www.gnu.org/licenses/>.
*/
package fr.parisrespire.mpp.data.http

import fr.parisrespire.mpp.base.IO
import fr.parisrespire.mpp.data.ExceptionWrapper
import fr.parisrespire.mpp.data.http.model.LaPoste
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.ParametersBuilder
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class GeoApi(
    private val client: HttpClient,
    private val networkConnectivity: NetworkConnectivity
) {

    constructor() : this(
        customHttpClient,
        NetworkConnectivityImpl
    )

    suspend fun requestInseeCode(postalCode: String): LaPoste {
        val argument = ParametersBuilder().apply {
            append("dataset", "laposte_hexasmal")
            append("facet", "code_commune_insee")
            append("facet", "code_postal")
            append("refine.code_postal", postalCode)
        }
        val urlBuilder = URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = HOST_GEO_API,
            encodedPath = PATH_SEARCH,
            parameters = argument
        )
        networkConnectivity.checkConnectivity()
        try {
            return withContext(IO) {
                val response = client.get<String> {
                    url(urlBuilder.buildString())
                }
                Json.parse(LaPoste.serializer(), response)
            }
        } catch (throwable: Throwable) {
            throw ExceptionWrapper(throwable).getCustomException()
        }
    }

}