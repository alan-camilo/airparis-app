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
        is CustomUnknownHostException -> context.getString(R.string.error_network).capitalize()
        is NoConnectivityException -> context.getString(R.string.no_connectivity).capitalize()
        is UnknownException -> context.getString(R.string.error_unknown).capitalize()
    }
}
