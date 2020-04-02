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
package fr.parisrespire

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.await
import fr.parisrespire.presenter.NotificationSettingsPresenter
import fr.parisrespire.work.NotificationWork.Companion.NOTIFICATION_WORK
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotificationSettingsPresenterTest {

    private lateinit var context: Context
    private lateinit var presenter: NotificationSettingsPresenter

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        presenter = NotificationSettingsPresenter(context)
    }

    @Test
    fun testSetNotifyPreferenceWithTrue() {
        presenter.setNotifyPreference(true)
        assertTrue(booleanMessage(true), presenter.getNotifyPreference())
        val workManager = WorkManager.getInstance(context)
        runBlocking {
            val future =
                workManager.getWorkInfosForUniqueWork(NOTIFICATION_WORK)
            val workInfo = future.await().firstOrNull()
            assertTrue("workInfo should not be null", workInfo != null)
            assertTrue("the work should be enqueued", workInfo?.state == WorkInfo.State.ENQUEUED)
        }
    }

    @Test
    fun testSetNotifyPreferenceWithFalse() {
        presenter.setNotifyPreference(false)
        assertTrue("expected value = false", !presenter.getNotifyPreference())
        val workManager = WorkManager.getInstance(context)
        runBlocking {
            val future =
                workManager.getWorkInfosForUniqueWork(NOTIFICATION_WORK)
            val workInfo = future.await().firstOrNull()
            assertTrue("expect non null workInfo", workInfo != null)
            assertTrue(
                "expect State.CANCELLED, actual is ${workInfo?.state}",
                workInfo?.state == WorkInfo.State.CANCELLED
            )
        }
    }

    @Test
    fun testSetAlertPreferenceWithTrue() {
        presenter.setAlertPreference(true)
        assertTrue(booleanMessage(true), presenter.getAlertPreference())
    }

    @Test
    fun testSetAlertPreferenceWithFalse() {
        presenter.setAlertPreference(false)
        assertFalse(booleanMessage(false), presenter.getAlertPreference())
    }

    @Test
    fun testSetTimePreference() {
        val time = DateTime().withTime(10, 30, 0, 0)
        presenter.setTimePreference(time.millis)
        val timeHour = presenter.getTimeHour()
        assertTrue("expected value is 10:30, actual value of timeHour is $timeHour", timeHour == "10:30")
        val workManager = WorkManager.getInstance(context)
        runBlocking {
            val future =
                workManager.getWorkInfosForUniqueWork(NOTIFICATION_WORK)
            val workInfo = future.await().firstOrNull()
            assertTrue("expect non null workInfo", workInfo != null)
            assertTrue(
                "expect State.ENQUEUED, actual is ${workInfo?.state}",
                workInfo?.state == WorkInfo.State.ENQUEUED
            )
        }
    }

    private fun booleanMessage(value: Boolean) = "expected value is $value, actual value is ${!value}"
}
