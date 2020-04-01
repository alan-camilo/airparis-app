/*
This file is part of airparis.

airparis is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or any 
later version.

airparis is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with airparis.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.airparis.model

import android.content.Context
import android.util.Log
import com.airparis.SHARED_PREFERENCES
import com.airparis.util.ALERT_SHARED_PREFERENCE
import com.airparis.util.NOTIFICATION_SHARED_PREFERENCE
import com.airparis.util.TIME_SHARED_PREFERENCE
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Interval
import org.joda.time.format.DateTimeFormat

class NotificationSettingsModel(context: Context) {

    private val formatter = DateTimeFormat.forPattern("HH:mm")
    private val sharedPref = context.getSharedPreferences(
        SHARED_PREFERENCES, Context.MODE_PRIVATE
    )
    var timePreference: Long = 0L
        set(value) {
            with(sharedPref.edit()) {
                putLong(TIME_SHARED_PREFERENCE, value)
                apply()
            }
            field = value
        }
    var isNotified: Boolean = false
        set(value) {
            with(sharedPref.edit()) {
                putBoolean(NOTIFICATION_SHARED_PREFERENCE, value)
                apply()
            }
            field = value
        }
    var isAlerted: Boolean = false
        set(value) {
            with(sharedPref.edit()) {
                putBoolean(ALERT_SHARED_PREFERENCE, value)
                apply()
            }
            field = value
        }

    //Initialize preferences with SharedPreferences values or default values
    init {
        val pref = sharedPref.getLong(TIME_SHARED_PREFERENCE, 0L)
        timePreference = if (pref == 0L) {
            DateTime().withTime(9, 0, 0, 0).millis
        } else {
            pref
        }
        isNotified = sharedPref.getBoolean(NOTIFICATION_SHARED_PREFERENCE, false)
        isAlerted = sharedPref.getBoolean(ALERT_SHARED_PREFERENCE, true)
    }

    fun getNotificationDelay(): Long {
        val now = DateTime()
        while (timePreference < now.millis) {
            timePreference = DateTime(timePreference).plusDays(1).millis
        }
        val delay = timePreference - now.millis
        Log.d(
            NotificationSettingsModel::class.simpleName,
            "now=${now.toString(formatter)} dueDate=${DateTime(timePreference).toString(formatter)} interval en minutes=${delay / 1000 / 60}"
        )
        return delay
    }

    fun getTimeHour() =  DateTime(timePreference).toString(formatter)
}