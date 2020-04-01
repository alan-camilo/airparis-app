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
package com.airparis.work

import airparis.data.http.AirparifAPI
import airparis.data.http.model.util.Day
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.work.*
import androidx.work.ListenableWorker.Result.success
import com.airparis.R
import com.airparis.SHARED_PREFERENCES
import com.airparis.activity.MainActivity
import com.airparis.util.TIME_SHARED_PREFERENCE
import com.airparis.util.indexToHumanReadableString
import com.airparis.util.scheduleNotification
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class NotificationWork(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params) {
    private val notification_id = 1
    private val alert_id = 2

    override fun doWork(): Result {
        val baseMessage = context.getString(R.string.notification_text)
        GlobalScope.launch {
            val indexList = AirparifAPI().requestIndex()
            val indexToday = indexList.firstOrNull { it.date == Day.TODAY.value }?.indice
            Log.d(NotificationWork::class.simpleName, "indexList = $indexList\nindexToday=$indexToday")
            if (indexToday != null) {
                try {
                    val humanReadable = indexToHumanReadableString(indexToday, context)
                    val formattedMessage = String.format(baseMessage, humanReadable, indexToday)
                    sendNotification(notification_id, formattedMessage)
                } catch (throwable: Throwable) {
                    Log.e(NotificationWork::class.simpleName, "message=${throwable.message} cause=${throwable.cause}")
                }
            } else {
                sendNotification(notification_id, "ERROR INDEXTODAY NULL")
            }
        }
        setNextDayNotification()
        return success()
    }

    private fun setNextDayNotification() {
        // enqueue next day notif
        val sharedPreferences = context.getSharedPreferences(
            SHARED_PREFERENCES, Context.MODE_PRIVATE
        )
        var timePreference = sharedPreferences.getLong(TIME_SHARED_PREFERENCE, 0L)
        while (timePreference < DateTime().millis) {
            timePreference = DateTime(timePreference).plusDays(1).millis
        }
        val delay = timePreference - DateTime().millis
        scheduleNotification(context, delay)
    }

    private fun sendNotification(id: Int, notificationContentText: String) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val titleNotification = applicationContext.getString(R.string.app_name)
        val pendingIntent = getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.oxygen_24)
            .setContentTitle(titleNotification)
            .setContentText(notificationContentText)
            .setContentIntent(pendingIntent)
            .setPriority(PRIORITY_HIGH)
            .setAutoCancel(true)

        if (SDK_INT >= O) {
            notification.setChannelId(NOTIFICATION_CHANNEL_ID)
            val channelDescription =
                applicationContext.getString(R.string.notification_channel_description)
            val channelName = applicationContext.getString(R.string.notification_channel_name)
            val channel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = channelDescription
                }
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notification.build())
    }

    companion object {
        const val NOTIFICATION_ID = "airparis_notification_id"
        const val NOTIFICATION_CHANNEL_ID = "airparis_channel_01"
        const val NOTIFICATION_WORK = "airparis_notification_work"
    }
}
