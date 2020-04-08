package fr.parisrespire

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.parisrespire.work.AlertWorkUtil
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import parisrespire.data.http.model.Episode
import parisrespire.data.http.model.PollutantDetails
import parisrespire.data.http.model.util.Day
import parisrespire.data.http.model.util.PollutionLevel

@RunWith(AndroidJUnit4::class)
class AlertWorkUtilTest {

    private lateinit var context: Context
    private lateinit var util: AlertWorkUtil
    private val episode = Episode(
        date = Day.TOMORROW.value,
        detail = "Il est conseillé d'éviter les déplacements en Ile de France",
        o3 = PollutantDetails(listOf("km", "pop"), "info", "constate"),
        no2 = PollutantDetails(listOf("km", "pop"), "info", "constate"),
        pm10 = PollutantDetails(listOf("km"), "alerte", "constate")
    )

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        util = AlertWorkUtil(context)
    }

    @Test
    fun testEpisodeToPollutantList() {
        val list = util.episodeToPollutantList(episode)
        val pm10 = list.find { it.first == context.getString(R.string.pm10) }
        val so2 = list.find { it.first == context.getString(R.string.so2) }
        val no2 = list.find { it.first == context.getString(R.string.no2) }
        val o3 = list.find { it.first == context.getString(R.string.o3) }
        assertNotNull(pm10)
        assertNotNull(no2)
        assertNotNull(o3)
        assertNull(so2)
        assertEquals(PollutionLevel.ALERT.value, pm10?.second?.niveau)
        assertEquals(PollutionLevel.INFO.value, o3?.second?.niveau)
        assertEquals(PollutionLevel.INFO.value, no2?.second?.niveau)
    }

    @Test
    fun testFormatAlertMessage() {
        val alertPart = String.format(
            context.resources.getQuantityString(R.plurals.alert_message, 1),
            context.getString(R.string.alert),
            context.getString(R.string.pm10)
        ).capitalize()
        val pollutants = "${context.getString(R.string.o3)}, ${context.getString(fr.parisrespire.R.string.no2)}"
        val infoPart = String.format(
            context.resources.getQuantityString(R.plurals.alert_message, 2),
            context.getString(R.string.information),
            pollutants
        ).capitalize()
        val expectedMessage =
            "$alertPart<br>$infoPart<br>Il est conseillé d'éviter les déplacements en Ile de France"
        val list = util.episodeToPollutantList(episode)
        val actual = util.formatAlertMessage(list, episode.detail)
        assertTrue(actual.second)
        assertEquals("expected=$expectedMessage actual=$actual", expectedMessage, actual.first)
    }
}
