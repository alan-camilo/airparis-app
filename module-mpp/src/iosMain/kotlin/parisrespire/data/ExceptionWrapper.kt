package parisrespire.data

actual class ExceptionWrapper actual constructor(throwable: Throwable) {
    actual fun getCustomException(): CustomException {
        TODO("Not yet implemented")
    }
}
