package com.airparis.activity

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.airparis.R
import com.airparis.presenter.NotificationSettingsPresenter
import com.airparis.fragment.TimePickerFragment
import kotlinx.android.synthetic.main.activity_notification_settings.*

const val timePreference = "time_preference"
const val notificationPreference = "notification_preference"
const val alertPreference = "alert_preference"

class NotificationSettingsActivity : AppCompatActivity() {

    private lateinit var presenter: NotificationSettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)
        presenter = NotificationSettingsPresenter(this)
        btn_time_picker.text = presenter.getTimePreference()
        btn_time_picker.isEnabled = presenter.getNotifyPreference()
        checkbox_alerts.isChecked =  presenter.getAlertPreference()
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

    fun onTimeSet(hourOfDay: Int, minute: Int) {
        val fullHour = "$hourOfDay:$minute"
        presenter.setNotificationCalendar(fullHour)
        btn_time_picker.text = fullHour
    }
}

