package fr.parisrespire.mpp.base

import android.content.Context
import android.content.SharedPreferences

actual object UserPreference {

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) = run {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    actual fun getBoolean(key: String, defValue: Boolean): Boolean {
        checkSharedPreferences()
        return sharedPreferences.getBoolean(key, defValue)
    }

    actual fun getLong(key: String, defValue: Long): Long {
        checkSharedPreferences()
        return sharedPreferences.getLong(key, defValue)
    }

    actual fun getString(key: String, defValue: String?): String? {
        checkSharedPreferences()
        return sharedPreferences.getString(key, defValue)
    }

    actual fun set(key: String, value: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    actual fun set(key: String, value: Long) {
        with(sharedPreferences.edit()) {
            putLong(key, value)
            apply()
        }
    }

    actual fun set(key: String, value: String?) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    private fun checkSharedPreferences() {
        if (!this::sharedPreferences.isInitialized)
            throw RuntimeException("Call UserPreference.init(context) in your Application class")
    }
}
