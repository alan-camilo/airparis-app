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
package fr.parisrespire.presenter

import android.content.Context
import android.util.Log
import androidx.work.*
import fr.parisrespire.mpp.data.NotificationSettingsModel
import fr.parisrespire.mpp.presenter.NotificationSettingsPresenter
import fr.parisrespire.util.scheduleAlert
import fr.parisrespire.util.scheduleNotification
import fr.parisrespire.util.unscheduleAlert
import fr.parisrespire.util.unscheduleNotification
import fr.parisrespire.work.NotificationWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class NotificationSettingsPresenterImpl(private val context: Context) :
    NotificationSettingsPresenter {

    private val model = NotificationSettingsModel()

    override fun setNotifyPreference(value: Boolean) {
        model.isNotified = value
        if (value) {
            scheduleNotification(model.getNotificationDelay())
        } else {
            unscheduleNotification()
        }
        // For debugging
        GlobalScope.launch(Dispatchers.Main) {
            val workManager = WorkManager.getInstance(context)
            val future =
                workManager.getWorkInfosForUniqueWork(NotificationWork.NOTIFICATION_WORK)
            val workInfo = future.await().firstOrNull()
            Log.d(NotificationSettingsPresenter::class.simpleName, "work state=${workInfo?.state} " +
                    "\noutputData=${workInfo?.outputData}\nprogress=${workInfo?.progress}")
        }
    }

    override fun setAlertPreference(value: Boolean) {
        model.isAlerted = value
        if (value) {
            scheduleAlert(context)
        } else {
            unscheduleAlert(context)
        }
    }

    override fun setTimePreference(hourOfDay: Int, minute: Int) {
        val dateTime = DateTime().withTime(hourOfDay, minute, 0, 0)
            .withZone(DateTimeZone.getDefault())
        model.timePreference = dateTime.millis
        scheduleNotification(model.getNotificationDelay())
    }

    override fun getTimeHour(): String = model.getTimeHour()

    override fun getNotifyPreference() = model.isNotified

    override fun getAlertPreference() = model.isAlerted

    private fun scheduleNotification(delay: Long) {
        Log.d(NotificationSettingsPresenter::class.simpleName, "scheduleNotification delay=$delay hours=${delay / 1000 / 3600}")
        scheduleNotification(context, delay)
    }

    private fun unscheduleNotification() {
        unscheduleNotification(context)
    }
}
