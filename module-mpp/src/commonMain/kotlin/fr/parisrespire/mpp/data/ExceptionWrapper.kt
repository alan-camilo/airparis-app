package fr.parisrespire.mpp.data

import fr.parisrespire.mpp.data.CustomException

expect class ExceptionWrapper(throwable: Throwable) {

    fun getCustomException(): CustomException
}
