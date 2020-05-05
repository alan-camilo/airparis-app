/*
This file is part of Paris respire.

Paris respire is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or any
later version.

Paris respire is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Paris respire.  If not, see <https://www.gnu.org/licenses/>.
*/
package fr.parisrespire.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.crashlytics.android.Crashlytics
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import fr.parisrespire.R
import fr.parisrespire.fragment.CollectionAirQualityFragment
import fr.parisrespire.mpp.base.ALERT_SHARED_PREFERENCE
import fr.parisrespire.mpp.base.CITY_NAME_PREFERENCE
import fr.parisrespire.mpp.base.INSEE_CODE_PREFERENCE
import fr.parisrespire.mpp.base.SHARED_PREFERENCES
import fr.parisrespire.mpp.data.CustomException
import fr.parisrespire.mpp.data.UIExceptionHandler
import fr.parisrespire.util.InseeCodeProvider
import fr.parisrespire.util.TAB_ARG
import fr.parisrespire.util.scheduleAlert
import java.util.*

class MainActivity : AppCompatActivity(), UIExceptionHandler {

    private var permissionDialog: AlertDialog? = null
    private val REQUEST_LOCATION_PERMISSION = 42
    private val IS_REQUESTING_PERMISSION = "42"
    private var isRequestingPermission = false
    private val PLAY_SERVICES_ERROR = 43

    // Insee city code for Paris 75001
    private val defaultInseeCode = "75056"
    private val defaultCity = "Paris"
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        savedInstanceState?.let {
            isRequestingPermission = it.getBoolean(IS_REQUESTING_PERMISSION)
        }
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val isAlerted = sharedPreferences.getBoolean(ALERT_SHARED_PREFERENCE, true)
        if (isAlerted) {
            scheduleAlert(this)
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onResume() {
        super.onResume()
        val instance = GoogleApiAvailability.getInstance()
        val result = instance.isGooglePlayServicesAvailable(this)
        if (result != ConnectionResult.SUCCESS) {
            instance.getErrorDialog(this, result, PLAY_SERVICES_ERROR)
        }
        if (hasLocationPermission()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    provideInseeCode(location)
                }
                .addOnFailureListener {
                    handleLocationFailure(it)
                }
        }
    }

    override fun onStop() {
        super.onStop()
        permissionDialog?.dismiss()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(IS_REQUESTING_PERMISSION, isRequestingPermission)
        super.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        Log.d(MainActivity::class.simpleName, "permission result")
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                isRequestingPermission = false
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, now retrieve insee city code
                    Log.d(MainActivity::class.simpleName, "permission granted")
                    permissionDialog?.dismiss()
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            provideInseeCode(location)
                        }
                        .addOnFailureListener {
                            handleLocationFailure(it)
                        }
                } else {
                    Log.d(MainActivity::class.simpleName, "permission refused")
                    showPermissionDialog()
                }
                return
            }
            else -> Unit
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.notification -> {INSEE_CODE_PREFERENCE
                val intent = Intent(this, NotificationSettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.refresh -> {
                refreshCollectionFragment()
                true
            }
            R.id.donate -> {
                val webIntent: Intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=X3Q8Y8E5CPNN6&source=url")
                )
                startActivity(webIntent)
                true
            }
            R.id.rate -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setFragment() {
        val tabIndex = intent.getIntExtra(TAB_ARG, 1)
        with(supportFragmentManager) {
            var fragment = this.findFragmentByTag("collection")
            if (fragment == null) {
                val transaction = this.beginTransaction()
                fragment = CollectionAirQualityFragment()
                fragment.arguments = Bundle().apply {
                    putInt(TAB_ARG, tabIndex)
                }
                transaction.add(R.id.container, fragment, "collection")
                transaction.commit()
            }
        }
    }

    private fun refreshCollectionFragment() {
        Log.d(MainActivity::class.simpleName, "refreshCollectionFragment")
        val fragment = supportFragmentManager.fragments.first() as CollectionAirQualityFragment
        fragment.refresh()
    }

    private fun hasLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            if (!isRequestingPermission)
                showPermissionDialog()
            return false
        }
        return true
    }

    private fun showPermissionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.location_permission_message)
            ?.setPositiveButton(R.string.share_location) { _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
                isRequestingPermission = true
            }
        builder.setCancelable(false)
        permissionDialog = builder.show()
    }

    override fun showError(exception: CustomException) {
        handleLocationFailure(exception)
    }

    private fun provideInseeCode(location: Location?) {
        if (location == null) {
            handleLocationFailure(null)
            return
        }
        val geocoder = Geocoder(this, Locale.FRANCE)
        var list: List<Address>? = null
        try {
            list = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        } catch (exception: Exception) {
            handleLocationFailure(exception)
            return
        }
        val address = list?.firstOrNull()
        if (address != null && address.adminArea == "Île-de-France") {
            with(getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).edit()) {
                // save city name
                putString(CITY_NAME_PREFERENCE, address.locality)
                apply()
            }
            // get insee code
            val postalCode = address.postalCode
            val provider = InseeCodeProvider(this)
            provider.inseeCode.addObserver {
                setFragment()
            }
            provider.getInseeCode(postalCode)
        } else {
            handleLocationFailure(null)
        }
    }

    private fun handleLocationFailure(exception: Throwable?) {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(INSEE_CODE_PREFERENCE, defaultInseeCode)
            apply()
        }
        if (exception != null)
            Crashlytics.logException(exception)
        setFragment()
    }
}
