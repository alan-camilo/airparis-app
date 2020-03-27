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
package com.airparis.presenter

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.airparis.model.NotificationSettingsModel
import com.airparis.work.NotifyWork
import java.util.concurrent.TimeUnit

class NotificationSettingsPresenter(private val context: Context) {

    private val model = NotificationSettingsModel(context)

    fun setNotifyPreference(value: Boolean) {
        model.isNotified = value
        if (value) {
            scheduleNotification(model.getNotificationDelay())
        }
    }

    fun setAlertPreference(value: Boolean) {
        model.isAlerted = value
    }

    fun setNotificationCalendar(fullHour: String) {
        model.timePreference = fullHour
        scheduleNotification(model.getNotificationDelay())
    }

    fun getTimePreference(): String = model.timePreference

    fun getNotifyPreference() = model.isNotified

    fun getAlertPreference() = model.isAlerted

    private fun scheduleNotification(delay: Long) {
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()
        val instanceWorkManager = WorkManager.getInstance(context)
        instanceWorkManager.beginUniqueWork(
            NotifyWork.NOTIFICATION_WORK,
            ExistingWorkPolicy.REPLACE, notificationWork
        ).enqueue()
    }

    private fun subscribeAlerts() {}
}




