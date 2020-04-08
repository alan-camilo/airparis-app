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
package parisrespire.test.data.http

import io.ktor.client.features.ClientRequestException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlinx.serialization.json.JsonDecodingException
import parisrespire.base.runBlocking
import parisrespire.data.http.AirparifAPI
import parisrespire.data.http.model.util.Day
import parisrespire.data.http.model.util.PollutionLevel

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
            assertEquals(result.first().so2?.niveau, PollutionLevel.ALERT.value)
            assertEquals(
                result.last().detail,
                "Il est conseillé d'éviter les déplacements en Ile de France"
            )
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
