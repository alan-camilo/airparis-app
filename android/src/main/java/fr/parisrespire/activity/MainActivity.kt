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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import fr.parisrespire.R
import fr.parisrespire.fragment.CollectionAirQualityFragment
import fr.parisrespire.util.scheduleAlert
import parisrespire.base.ALERT_SHARED_PREFERENCE
import parisrespire.base.SHARED_PREFERENCES

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences = getSharedPreferences(
            SHARED_PREFERENCES, Context.MODE_PRIVATE
        )
        val isAlerted = sharedPreferences.getBoolean(ALERT_SHARED_PREFERENCE, true)
        if (isAlerted) {
            scheduleAlert(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
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

    private fun refreshCollectionFragment() {
        Log.d(MainActivity::class.simpleName, "refreshCollectionFragment")
        val fragment = supportFragmentManager.fragments.first() as CollectionAirQualityFragment
        fragment.refresh()
    }
}
