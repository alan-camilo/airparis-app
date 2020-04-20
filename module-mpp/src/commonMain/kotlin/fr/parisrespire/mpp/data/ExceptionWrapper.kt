package fr.parisrespire.mpp.data

expect class ExceptionWrapper(throwable: Throwable) {

    fun getCustomException(): CustomException
}
