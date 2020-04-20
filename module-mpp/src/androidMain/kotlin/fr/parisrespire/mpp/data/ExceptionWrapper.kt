package fr.parisrespire.mpp.data

import io.ktor.client.features.*
import java.net.SocketTimeoutException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonException

actual class ExceptionWrapper actual constructor(throwable: Throwable) {

    private val throwable = throwable

    actual fun getCustomException(): CustomException {
        return when (throwable) {
            is JsonException -> CustomJsonException(
                throwable
            )
            is SerializationException -> CustomSerializationException(
                throwable
            )
            is ClientRequestException -> CustomClientRequestException(
                throwable
            )
            is HttpRequestTimeoutException -> CustomHttpRequestTimeoutException(
                throwable
            )
            is RedirectResponseException -> CustomRedirectResponseException(
                throwable
            )
            is SendCountExceedException -> CustomSendCountExceedException(
                throwable
            )
            is ServerResponseException -> CustomServerResponseException(
                throwable
            )
            is SocketTimeoutException -> CustomSocketTimeoutException(
                throwable
            )
            else -> UnknownException(throwable)
        }
    }
}
