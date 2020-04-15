package parisrespire.data

sealed class CustomException(val throwable: Throwable) : Throwable()

class CustomJsonException(throwable: Throwable) : CustomException(throwable)

class CustomSerializationException(throwable: Throwable) : CustomException(throwable)

class CustomClientRequestException(throwable: Throwable) : CustomException(throwable)

class CustomHttpRequestTimeoutException(throwable: Throwable) : CustomException(throwable)

class CustomRedirectResponseException(throwable: Throwable) : CustomException(throwable)

class CustomSendCountExceedException(throwable: Throwable) : CustomException(throwable)

class CustomServerResponseException(throwable: Throwable) : CustomException(throwable)

class CustomSocketTimeoutException(throwable: Throwable) : CustomException(throwable)

class UnknownException(throwable: Throwable) : CustomException(throwable)
