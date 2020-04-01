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
package com.airparis.util

import android.content.Context
import android.util.Log
import androidx.work.*
import com.airparis.R
import com.airparis.presenter.NotificationSettingsPresenter
import com.airparis.work.NotificationWork
import java.util.concurrent.TimeUnit

fun indexToHumanReadableString(index: Int, context: Context): String? {
    return when {
        index in 0..24 -> context.getString(R.string.very_low)
        index in 25..49 -> context.getString(R.string.low)
        index in 50..74 -> context.getString(R.string.mediocre)
        index in 75..99 -> context.getString(R.string.high)
        index > 100 -> context.getString(R.string.very_high)
        else -> null
    }
}

const val TIME_SHARED_PREFERENCE = "time_shared_preferences"
const val NOTIFICATION_SHARED_PREFERENCE = "notification_shared_preferences"
const val ALERT_SHARED_PREFERENCE = "alert_shared_preferences"

fun scheduleNotification(context: Context, delay: Long) {
    val constraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
    val notificationWork = OneTimeWorkRequest.Builder(NotificationWork::class.java)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setConstraints(constraint)
        .build()
    val instanceWorkManager = WorkManager.getInstance(context)
    instanceWorkManager.beginUniqueWork(
        NotificationWork.NOTIFICATION_WORK,
        ExistingWorkPolicy.REPLACE, notificationWork
    ).enqueue()
}

fun unscheduleNotification(context: Context) {
    val instanceWorkManager = WorkManager.getInstance(context)
    instanceWorkManager.cancelUniqueWork(NotificationWork.NOTIFICATION_WORK)
}