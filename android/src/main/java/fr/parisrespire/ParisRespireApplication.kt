package fr.parisrespire

import android.app.Application
import fr.parisrespire.mpp.base.UserPreference
import fr.parisrespire.mpp.data.http.NetworkConnectivity

class ParisRespireApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        UserPreference.init(applicationContext)
        NetworkConnectivity.init(applicationContext)
    }
}
