package fr.parisrespire.mpp.data

import com.github.florent37.log.Logger
import com.soywiz.klock.*
import fr.parisrespire.mpp.base.ALERT_SHARED_PREFERENCE
import fr.parisrespire.mpp.base.NOTIFICATION_SHARED_PREFERENCE
import fr.parisrespire.mpp.base.TIME_SHARED_PREFERENCE
import fr.parisrespire.mpp.base.UserPreference

class NotificationSettingsModel {

    private val formatter = DateFormat("HH:mm")

    var timePreference: Long = 0L
        set(value) {
            UserPreference.set(TIME_SHARED_PREFERENCE, value)
            field = value
        }
    var isNotified: Boolean = false
        set(value) {
            UserPreference.set(NOTIFICATION_SHARED_PREFERENCE, value)
            field = value
        }
    var isAlerted: Boolean = false
        set(value) {
            UserPreference.set(ALERT_SHARED_PREFERENCE, value)
            field = value
        }

    // Initialize preferences with SharedPreferences values or default values
    init {
        val pref = UserPreference.getLong(
            TIME_SHARED_PREFERENCE, 0L)
        timePreference = if (pref == 0L) {
            val dateTimeStr = DateTimeTz.nowLocal().toString("dd MMM yyyy z") + " 09:00:00"
            DateFormat("dd MMM yyyy z HH:mm:ss").parse(dateTimeStr).local.unixMillisLong
        } else {
            pref
        }
        isNotified = UserPreference.getBoolean(
            NOTIFICATION_SHARED_PREFERENCE, false)
        isAlerted = UserPreference.getBoolean(
            ALERT_SHARED_PREFERENCE, true)
    }

    fun getNotificationDelay(): Long {
        val now = DateTime.nowUnixLong()
        while (timePreference < now) {
            timePreference += 1.days.millisecondsLong
        }
        val delay = timePreference - now
        Logger.d(
            "NotificationSettingsModel",
            "now=${DateTime(timePreference).local.format(formatter)} dueDate=${DateTime(
                timePreference
            ).local.format(formatter)} interval en minutes=${delay / 1000 / 60}"
        )
        return delay
    }

    fun getTimeHour(): String = DateTime(timePreference).local.format("HH:mm")
}
