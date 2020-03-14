package test.http

import base.runBlocking
import http.AirparifAPI
import http.mockResponseBadJson
import http.mockResponseBadRequest
import http.mockResponseOK
import http.util.Day
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
            assertFailsWith<ClientRequestException> { AirparifAPI(mockResponseBadRequest).requestIndex() }
        }
    }

    @Test
    fun testRequestIndexParserException() {
        runBlocking {
            assertFailsWith<JsonDecodingException> { AirparifAPI(mockResponseBadJson).requestIndex() }
        }
    }
    // //////////////////

    // Request indiceJour
    @Test
    fun testRequestDayIndex() {
        runBlocking {
            val result = AirparifAPI(mockResponseOK).requestDayIndex(Day.TODAY)
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
