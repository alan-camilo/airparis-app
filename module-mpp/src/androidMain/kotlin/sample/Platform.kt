package sample

actual object Platform {
    actual val name: String
        get() = "Android"
}

actual class Sample actual constructor() {
    actual fun checkMe(): Int {
        return 42
    }
}
