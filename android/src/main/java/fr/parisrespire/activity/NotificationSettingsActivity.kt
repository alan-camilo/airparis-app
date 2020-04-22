package fr.parisrespire.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import fr.parisrespire.R
import fr.parisrespire.databinding.ActivityNotificationSettingsBinding
import fr.parisrespire.fragment.TimePickerFragment
import fr.parisrespire.mpp.presenter.NotificationSettingsPresenter
import fr.parisrespire.presenter.NotificationSettingsPresenterImpl
import kotlinx.android.synthetic.main.activity_notification_settings.*

class NotificationSettingsActivity : AppCompatActivity() {

    private lateinit var presenter: NotificationSettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)
        presenter = NotificationSettingsPresenterImpl(this)
        // Binding layout and the presenter
        val binding: ActivityNotificationSettingsBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_notification_settings
        )
        binding.presenter = presenter
        // Back button
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = null
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        return true
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
        presenter.setTimePreference(hourOfDay, minute)
        btn_time_picker.text = presenter.getTimeHour()
    }
}
