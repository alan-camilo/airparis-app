package fr.parisrespire

import android.app.Application
import parisrespire.base.UserPreference

class ParisRespireApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        UserPreference.init(applicationContext)
    }
}
