package parisrespire.data

import io.ktor.client.features.*

expect class ExceptionWrapper(throwable: Throwable) {

    fun getCustomException(): CustomException
}
