package fr.parisrespire.mpp.data

import io.ktor.client.features.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonException

actual class ExceptionWrapper actual constructor(throwable: Throwable) {

    private val throwable = throwable

    actual fun getCustomException(): CustomException {
        return when (throwable) {
            is JsonException -> CustomJsonException(throwable.message, throwable.cause)
            is SerializationException -> CustomSerializationException(
                throwable.message,
                throwable.cause
            )
            is ClientRequestException -> CustomClientRequestException(
                throwable.message,
                throwable.cause
            )
            is HttpRequestTimeoutException -> CustomHttpRequestTimeoutException(
                throwable.message,
                throwable.cause
            )
            is RedirectResponseException -> CustomRedirectResponseException(
                throwable.message,
                throwable.cause
            )
            is SendCountExceedException -> CustomSendCountExceedException(
                throwable.message,
                throwable.cause
            )
            is ServerResponseException -> CustomServerResponseException(
                throwable.message,
                throwable.cause
            )
            is SocketTimeoutException -> CustomSocketTimeoutException(
                throwable.message,
                throwable.cause
            )
            is UnknownHostException -> CustomUnknownHostException(
                throwable.message,
                throwable.cause
            )
            else -> UnknownException(throwable.message, throwable.cause)
        }
    }
}
