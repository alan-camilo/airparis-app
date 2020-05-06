package fr.parisrespire

import android.app.Application
import fr.parisrespire.mpp.base.UserPreference
import fr.parisrespire.mpp.data.http.NetworkConnectivityImpl

class ParisRespireApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        UserPreference.init(applicationContext)
        NetworkConnectivityImpl.init(applicationContext)
    }
}
