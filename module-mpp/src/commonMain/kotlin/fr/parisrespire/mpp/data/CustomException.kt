package fr.parisrespire.mpp.data

sealed class CustomException(override val message: String?, override val cause: Throwable?) : Throwable()

class CustomJsonException(message: String?, cause: Throwable?) : CustomException(message, cause)

class CustomSerializationException(message: String?, cause: Throwable?) : CustomException(message, cause)

class CustomClientRequestException(message: String?, cause: Throwable?) : CustomException(message, cause)

class CustomHttpRequestTimeoutException(message: String?, cause: Throwable?) : CustomException(message, cause)

class CustomRedirectResponseException(message: String?, cause: Throwable?) : CustomException(message, cause)

class CustomSendCountExceedException(message: String?, cause: Throwable?) : CustomException(message, cause)

class CustomServerResponseException(message: String?, cause: Throwable?) : CustomException(message, cause)

class UnknownException(message: String?, cause: Throwable?) : CustomException(message, cause)

sealed class CustomIOException(message: String?, cause: Throwable?) : CustomException(message, cause)

class CustomSocketTimeoutException(message: String?, cause: Throwable?) : CustomIOException(message, cause)

class CustomUnknownHostException(message: String?, cause: Throwable?) : CustomIOException(message, cause)

class NoConnectivityException(message: String?, cause: Throwable?) : CustomIOException(message, cause)
