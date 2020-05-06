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
package fr.parisrespire.mpp.test.data.http

import fr.parisrespire.mpp.base.runBlocking
import fr.parisrespire.mpp.data.CustomClientRequestException
import fr.parisrespire.mpp.data.CustomJsonException
import fr.parisrespire.mpp.data.http.GeoApi
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class GeoApiTest {

    @Test
    fun testRequestCodeInsee() {
        runBlocking {
            val result = GeoApi(mockResponseOK, NetworkConnectivityMock()).requestInseeCode("91000")
            assertNotNull(result)
        }
    }

    @Test
    fun testRequestCodeInseeBadRequest() {
        runBlocking {
            assertFailsWith<CustomClientRequestException> {
                GeoApi(mockResponseBadRequest, NetworkConnectivityMock()).requestInseeCode("91000")
            }
        }
    }

    @Test
    fun testRequestCodeInseeParserException() {
        runBlocking {
            assertFailsWith<CustomJsonException> {
                GeoApi(mockResponseBadJson, NetworkConnectivityMock()).requestInseeCode("91000")
            }
        }
    }
}