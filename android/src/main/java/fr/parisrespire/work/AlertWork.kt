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
package fr.parisrespire.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.ListenableWorker.Result.success
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.crashlytics.android.Crashlytics
import fr.parisrespire.R
import fr.parisrespire.activity.MainActivity
import fr.parisrespire.util.scheduleAlert
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import fr.parisrespire.mpp.data.http.AirparifAPI
import fr.parisrespire.mpp.data.http.model.Episode
import fr.parisrespire.mpp.data.http.model.util.Day

class AlertWork(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    private val notificationId = 1
    private val util = AlertWorkUtil(context)

    override fun doWork(): Result {
        GlobalScope.launch {
            var tomorrow: Episode? = null
            try {
                tomorrow =
                    AirparifAPI().requestPollutionEpisode().find { it.date == Day.TOMORROW.value }
            } catch (throwable: Throwable) {
                Crashlytics.logException(throwable)
            }
            tomorrow?.let {
                val filtered = util.episodeToPollutantList(it)
                if (filtered.isNotEmpty()) {
                    sendNotification(util.formatAlertMessage(filtered, tomorrow.detail))
                }
            }
            setNextDayAlert()
        }
        return success()
    }

    private fun setNextDayAlert() {
        scheduleAlert(context)
    }

    private fun sendNotification(text: Pair<String, Boolean>) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val title = applicationContext.getString(R.string.alert_channel_name)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(
            applicationContext,
            ALERT_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.baseline_notification_important_24)
            .setContentTitle(title)
            .setContentText(text.first)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)

        if (text.second) {
            notification.setStyle(NotificationCompat.BigTextStyle().bigText(text.first))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(ALERT_CHANNEL_ID)
            val channel =
                NotificationChannel(
                    ALERT_CHANNEL_ID,
                    applicationContext.getString(R.string.alert_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = applicationContext.getString(R.string.alert_channel_description)
                }
            channel.enableVibration(true)
            manager.createNotificationChannel(channel)
        }

        manager.notify(notificationId, notification.build())
    }

    companion object {
        const val ALERT_CHANNEL_ID = "paris_respire_channel_02"
        const val ALERT_WORK = "paris_respire_alert_work"
    }
}
