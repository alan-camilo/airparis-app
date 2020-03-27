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
import com.airparis.SHARED_PREFERENCES
import java.text.SimpleDateFormat
import java.util.*

const val timeSharedPreferences = "time_shared_preferences"
const val notificationSharedPreferences = "notification_shared_preferences"
const val alertSharedPreferences = "alert_shared_preferences"

class NotificationSettingsModel(context: Context) {

    private val sharedPref = context.getSharedPreferences(
        SHARED_PREFERENCES, Context.MODE_PRIVATE
    )
    private var notificationCalendar: Calendar = Calendar.getInstance()
    var timePreference: String = ""
        get() = SimpleDateFormat("HH:mm", Locale.FRANCE).format(notificationCalendar.time)
        set(value) {
            with(sharedPref.edit()) {
                putString(timeSharedPreferences, value)
                apply()
            }
            field = value
            notificationCalendar.time = SimpleDateFormat("HH:mm", Locale.FRANCE).parse(value)!!
        }
    var isNotified: Boolean = false
        set(value) {
            with(sharedPref.edit()) {
                putBoolean(notificationSharedPreferences, value)
                apply()
            }
            field = value
        }
    var isAlerted: Boolean = false
        set(value) {
            with(sharedPref.edit()) {
                putBoolean(alertSharedPreferences, value)
                apply()
            }
            field = value
        }

    //Initialize preferences with SharedPreferences values or default values
    init {
        val pref = sharedPref.getString(timeSharedPreferences, null)
        if (pref == null || pref == "") {
            timePreference = "09:00"
            notificationCalendar.time = SimpleDateFormat("HH:mm", Locale.FRANCE).parse("09:00")!!
        } else {
            timePreference = pref
            notificationCalendar.time = SimpleDateFormat("HH:mm", Locale.FRANCE).parse(pref)!!
        }
        isNotified = sharedPref.getBoolean(notificationSharedPreferences, false)
        isAlerted = sharedPref.getBoolean(alertSharedPreferences, true)
    }

    fun getNotificationDelay(): Long {
        val currentDate = Calendar.getInstance()
        while (notificationCalendar.before(currentDate)) {
            notificationCalendar.add(Calendar.HOUR_OF_DAY, 24)
        }
        return notificationCalendar.timeInMillis - currentDate.timeInMillis
    }
}