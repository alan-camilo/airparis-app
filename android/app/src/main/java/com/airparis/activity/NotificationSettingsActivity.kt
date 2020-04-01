package com.airparis.activity

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.airparis.R
import com.airparis.fragment.TimePickerFragment
import com.airparis.presenter.NotificationSettingsPresenter
import kotlinx.android.synthetic.main.activity_notification_settings.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

const val timePreference = "time_preference"
const val notificationPreference = "notification_preference"
const val alertPreference = "alert_preference"

class NotificationSettingsActivity : AppCompatActivity() {

    private lateinit var presenter: NotificationSettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)
        presenter = NotificationSettingsPresenter(this)
        btn_time_picker.text = presenter.getTimeHour()
        btn_time_picker.isEnabled = presenter.getNotifyPreference()
        checkbox_alerts.isChecked = presenter.getAlertPreference()
        checkbox_notifications.isChecked = presenter.getNotifyPreference()
    }

    fun onCheckBoxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.checkbox_notifications -> {
                    btn_time_picker.isEnabled = checked
                    presenter.setNotifyPreference(checked)
                }
                R.id.checkbox_alerts -> {
                    presenter.setAlertPreference(checked)
                }
            }
        }
    }

    fun showTimePickerDialog(v: View) {
        if (!v.isEnabled) return
        TimePickerFragment().show(supportFragmentManager, "timePicker")
    }

    fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val dateTime = DateTime().withTime(hourOfDay, minute, 0, 0)
            .withZone(DateTimeZone.getDefault())
        presenter.setTimePreference(dateTime.millis)
        btn_time_picker.text = presenter.getTimeHour()
    }
}

