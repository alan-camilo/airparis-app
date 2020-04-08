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
package parisrespire.test.data

import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.days
import kotlin.test.Test
import kotlin.test.assertTrue
import parisrespire.data.http.model.util.Day
import parisrespire.data.http.model.util.toDay

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
