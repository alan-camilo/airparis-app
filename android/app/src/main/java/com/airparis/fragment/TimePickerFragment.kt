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
package com.airparis.fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.airparis.ui.getHourComp
import com.airparis.ui.getMinuteComp
import com.airparis.work.NotifyWork
import com.airparis.work.NotifyWork.Companion.NOTIFICATION_ID
import java.util.*
import java.util.Calendar.*
import java.util.concurrent.TimeUnit

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        dueDate.set(HOUR_OF_DAY, view.getHourComp())
        dueDate.set(MINUTE, view.getMinuteComp())
        dueDate.set(SECOND, 0)

        if (dueDate.before(currentDate)) {
            dueDate.add(HOUR_OF_DAY, 24)
        }
        val data = Data.Builder().putInt(NOTIFICATION_ID, 0).build()
        val delay = dueDate.timeInMillis - currentDate.timeInMillis
        scheduleNotification(delay, data)
    }

    private fun scheduleNotification(delay: Long, data: Data) {
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()
        val instanceWorkManager = WorkManager.getInstance(context!!)
        instanceWorkManager.beginUniqueWork(
            NotifyWork.NOTIFICATION_WORK,
            ExistingWorkPolicy.REPLACE, notificationWork
        ).enqueue()
    }
}
