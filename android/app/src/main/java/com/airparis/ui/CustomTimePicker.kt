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
package com.airparis.ui

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.M
import android.widget.TimePicker


@Suppress("DEPRECATION")
fun TimePicker.getMinuteComp(): Int {
    return when {
        SDK_INT >= M -> this.minute
        else -> this.currentMinute
    }
}

@Suppress("DEPRECATION")
fun TimePicker.getHourComp(): Int {
    return when {
        SDK_INT >= M -> this.hour
        else -> this.currentHour
    }
}

@Suppress("DEPRECATION")
fun TimePicker.setMinuteComp(minute: Int) {
    when {
        SDK_INT >= M -> this.minute = minute
        else -> this.currentMinute = minute
    }
}

@Suppress("DEPRECATION")
fun TimePicker.setHourComp(hour: Int) {
    when {
        SDK_INT >= M -> this.hour = hour
        else -> this.currentHour = hour
    }
}