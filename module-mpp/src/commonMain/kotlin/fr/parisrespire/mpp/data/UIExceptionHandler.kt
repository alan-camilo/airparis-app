package fr.parisrespire.mpp.data

import fr.parisrespire.mpp.data.CustomException

interface UIExceptionHandler {
    fun showError(exception: CustomException)
}
