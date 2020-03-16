/*
This file is part of airparis.

airparis is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or any
later version.

airparis is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with airparis.  If not, see <https://www.gnu.org/licenses/>.
*/
package airparis.test.data.http

import airparis.base.runBlocking
import airparis.data.http.AirparifAPI
import airparis.data.http.model.util.Day
import io.ktor.client.features.ClientRequestException
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlinx.serialization.json.JsonDecodingException

class AirparifAPITest {

    // Request 'indice'
    @Test
    fun testRequestIndex() {
        runBlocking {
            val result = AirparifAPI(mockResponseOK).requestIndex()
            assertNotNull(result)
        }
    }

    @Test
    fun testRequestIndexBadRequest() {
        runBlocking {
            assertFailsWith<ClientRequestException> {
                AirparifAPI(
                    mockResponseBadRequest
                ).requestIndex()
            }
        }
    }

    @Test
    fun testRequestIndexParserException() {
        runBlocking {
            assertFailsWith<JsonDecodingException> {
                AirparifAPI(
                    mockResponseBadJson
                ).requestIndex()
            }
        }
    }
    // //////////////////

    // Request indiceJour
    @Test
    fun testRequestDayIndex() {
        runBlocking {
            val result = AirparifAPI(mockResponseOK)
                .requestDayIndex(Day.TODAY)
            assertNotNull(result)
        }
    }

    @Test
    fun testRequestDayIndexBadRequest() {
        runBlocking {
            assertFailsWith<ClientRequestException> {
                AirparifAPI(mockResponseBadRequest).requestDayIndex(
                    Day.TODAY
                )
            }
        }
    }

    @Test
    fun testRequestDayIndexParserException() {
        runBlocking {
            assertFailsWith<JsonDecodingException> {
                AirparifAPI(mockResponseBadJson).requestDayIndex(
                    Day.TODAY
                )
            }
        }
    }
    // ///////////////

    // Request episode pollution
    @Test
    fun testRequestPollutionEpisode() {
        runBlocking {
            val result = AirparifAPI(mockResponseOK).requestPollutionEpisode()
            assertNotNull(result)
        }
    }

    @Test
    fun testRequestPollutionEpisodeBadRequest() {
        runBlocking {
            assertFailsWith<ClientRequestException> {
                AirparifAPI(mockResponseBadRequest).requestPollutionEpisode()
            }
        }
    }

    @Test
    fun testRequestPollutionEpisodeParserException() {
        runBlocking {
            assertFailsWith<JsonDecodingException> {
                AirparifAPI(mockResponseBadJson).requestPollutionEpisode()
            }
        }
    }
}
