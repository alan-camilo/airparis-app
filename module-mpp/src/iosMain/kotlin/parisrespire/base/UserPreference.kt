package parisrespire.base

actual object UserPreference {

    actual fun getBoolean(key: String, defValue: Boolean): Boolean {
        return true
    }

    actual fun getLong(key: String, defValue: Long): Long {
        return 0L
    }

    actual fun set(key: String, value: Boolean) {
    }

    actual fun set(key: String, value: Long) {
    }
}
