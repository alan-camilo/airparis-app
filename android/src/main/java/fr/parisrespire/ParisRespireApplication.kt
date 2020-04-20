package fr.parisrespire

import android.app.Application
import fr.parisrespire.mpp.base.UserPreference

class ParisRespireApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        UserPreference.init(applicationContext)
    }
}
