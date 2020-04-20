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
import fr.parisrespire.mpp.data.http.AirparifAPI
import fr.parisrespire.mpp.data.http.model.util.Day
import fr.parisrespire.mpp.data.http.model.util.PollutionLevel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class AirparifAPITest {

    // Request 'indice'
    @Test
    fun testRequestIndex() {
        runBlocking {
            val result = AirparifAPI(mockResponseOK, ApiKeyRepositoryMock())
                .requestIndex()
            assertNotNull(result)
        }
    }

    @Test
    fun testRequestIndexBadRequest() {
        runBlocking {
            assertFailsWith<CustomClientRequestException> {
                AirparifAPI(
                    mockResponseBadRequest,
                    ApiKeyRepositoryMock()
                ).requestIndex()
            }
        }
    }

    @Test
    fun testRequestIndexParserException() {
        runBlocking {
            assertFailsWith<CustomJsonException> {
                AirparifAPI(
                    mockResponseBadJson,
                    ApiKeyRepositoryMock()
                ).requestIndex()
            }
        }
    }
    // //////////////////

    // Request indiceJour
    @Test
    fun testRequestDayIndex() {
        runBlocking {
            val result = AirparifAPI(mockResponseOK, ApiKeyRepositoryMock())
                .requestDayIndex(Day.TODAY)
            assertNotNull(result)
        }
    }

    @Test
    fun testRequestDayIndexBadRequest() {
        runBlocking {
            assertFailsWith<CustomClientRequestException> {
                AirparifAPI(mockResponseBadRequest, ApiKeyRepositoryMock())
                    .requestDayIndex(
                    Day.TODAY
                )
            }
        }
    }

    @Test
    fun testRequestDayIndexParserException() {
        runBlocking {
            assertFailsWith<CustomJsonException> {
                AirparifAPI(mockResponseBadJson, ApiKeyRepositoryMock()).requestDayIndex(
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
            val result = AirparifAPI(mockResponseOK, ApiKeyRepositoryMock())
                .requestPollutionEpisode()
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
            assertFailsWith<CustomClientRequestException> {
                AirparifAPI(mockResponseBadRequest, ApiKeyRepositoryMock())
                    .requestPollutionEpisode()
            }
        }
    }

    @Test
    fun testRequestPollutionEpisodeParserException() {
        runBlocking {
            assertFailsWith<CustomJsonException> {
                AirparifAPI(mockResponseBadJson, ApiKeyRepositoryMock()).requestPollutionEpisode()
            }
        }
    }
}
