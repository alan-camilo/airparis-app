/*
This file is part of Paris respire.

Paris respire is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or any 
later version.

Paris respire is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Paris respire.  If not, see <https://www.gnu.org/licenses/>.
*/
package fr.parisrespire.util

import android.content.Context
import androidx.work.*
import fr.parisrespire.R
import fr.parisrespire.work.AlertWork
import fr.parisrespire.work.NotificationWork
import java.util.concurrent.TimeUnit
import org.joda.time.DateTime

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

fun scheduleAlert(context: Context) {
    val constraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
    val notificationWork = OneTimeWorkRequest.Builder(AlertWork::class.java)
        .setInitialDelay(getAlertDelay(), TimeUnit.MILLISECONDS)
        .setConstraints(constraint)
        .build()
    val instanceWorkManager = WorkManager.getInstance(context)
    instanceWorkManager.beginUniqueWork(
        AlertWork.ALERT_WORK,
        ExistingWorkPolicy.KEEP, notificationWork
    ).enqueue()
}

fun unscheduleAlert(context: Context) {
    val instanceWorkManager = WorkManager.getInstance(context)
    instanceWorkManager.cancelUniqueWork(AlertWork.ALERT_WORK)
}

private fun getAlertDelay(): Long {
    var dueTime = DateTime().withTime(11, 5, 0, 0)
    val now = DateTime()
    if (dueTime.millis < now.millis) {
        dueTime = dueTime.plusDays(1)
    }
    return dueTime.millis - now.millis
}
