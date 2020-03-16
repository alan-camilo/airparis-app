/*
This file is part of airparis.

airparis is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or any 
later version.

airparis is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with airparis.  If not, see <https://www.gnu.org/licenses/>.
*/
package airparis.test.data

import airparis.data.http.model.util.Day
import airparis.data.http.model.util.toDay
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.days
import kotlin.test.Test
import kotlin.test.assertTrue

class DateTest {

    @Test
    fun `test YESTERDAY fun dateToDayObject`() {
        val yesterday = (DateTimeTz.nowLocal() - 1.days).format("dd/MM/yyyy")
        val day = yesterday.toDay()
        assertTrue {
            day == Day.YESTERDAY
        }
    }

    @Test
    fun `test TODAY fun dateToDayObject`() {
        val today = DateTimeTz.nowLocal().format("dd/MM/yyyy")
        val day = today.toDay()
        assertTrue {
            day == Day.TODAY
        }
    }

    @Test
    fun `test TOMMOROW fun dateToDayObject`() {
        val tomorrow = (DateTimeTz.nowLocal() + 1.days).format("dd/MM/yyyy")
        val day = tomorrow.toDay()
        assertTrue {
            day == Day.TOMORROW
        }
    }
}