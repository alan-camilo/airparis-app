package parisrespire.base

const val TIME_SHARED_PREFERENCE = "time_shared_preferences"
const val NOTIFICATION_SHARED_PREFERENCE = "notification_shared_preferences"
const val ALERT_SHARED_PREFERENCE = "alert_shared_preferences"
const val SHARED_PREFERENCES = "paris_respire_shared_preferences"

expect object UserPreference {

    fun getBoolean(key: String, defValue: Boolean): Boolean

    fun getLong(key: String, defValue: Long): Long

    fun set(key: String, value: Boolean)

    fun set(key: String, value: Long)
}
