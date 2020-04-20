package fr.parisrespire.mpp.presenter

interface NotificationSettingsPresenter {

    fun setNotifyPreference(value: Boolean)

    fun setAlertPreference(value: Boolean)

    fun setTimePreference(timeInMillis: Long)

    fun getTimeHour(): String

    fun getNotifyPreference(): Boolean

    fun getAlertPreference(): Boolean
}
