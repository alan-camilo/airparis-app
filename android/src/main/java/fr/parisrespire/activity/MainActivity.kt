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
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import fr.parisrespire.R
import fr.parisrespire.fragment.CollectionAirQualityFragment
import fr.parisrespire.mpp.base.ALERT_SHARED_PREFERENCE
import fr.parisrespire.mpp.base.SHARED_PREFERENCES
import fr.parisrespire.util.TAB_ARG
import fr.parisrespire.util.scheduleAlert

class MainActivity : AppCompatActivity() {

    private var permissionDialog: AlertDialog? = null
    private val REQUEST_LOCATION_PERMISSION = 420
    private val IS_REQUESTING_PERMISSION = "420"
    private var isRequestingPermission = false

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
    }

    override fun onStart() {
        super.onStart()
        if (hasLocationPermission())
            setFragment()
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
                    // permission was granted
                    Log.d(MainActivity::class.simpleName, "permission granted")
                    permissionDialog?.dismiss()
                    setFragment()
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
            R.id.notification -> {
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
}
