package fr.parisrespire.util

import android.content.Context
import fr.parisrespire.R
import fr.parisrespire.mpp.data.*

fun getErrorMessage(context: Context, exception: CustomException): String {
    return when (exception) {
        is CustomJsonException -> context.getString(R.string.error_bad_json).capitalize()
        is CustomSerializationException -> context.getString(R.string.error_serialization).capitalize()
        is CustomClientRequestException -> context.getString(R.string.error_bad_client_request).capitalize()
        is CustomHttpRequestTimeoutException -> context.getString(R.string.error_timeout).capitalize()
        is CustomRedirectResponseException -> context.getString(R.string.error_redirect).capitalize()
        is CustomSendCountExceedException -> context.getString(R.string.error_send_count).capitalize()
        is CustomServerResponseException -> context.getString(R.string.error_server).capitalize()
        is CustomSocketTimeoutException -> context.getString(R.string.error_socket_timeout).capitalize()
        is UnknownException -> context.getString(R.string.error_unknown).capitalize()
    }
}
