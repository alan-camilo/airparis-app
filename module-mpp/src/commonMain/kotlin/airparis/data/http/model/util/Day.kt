package airparis.data.http.model.util

import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.days

enum class Day(val value: String) {
    YESTERDAY("hier"),
    TODAY("jour"),
    TOMORROW("demain")
}

fun String.toDay(): Day? {
    val today = DateTimeTz.nowLocal()
    val yesterday = today - 1.days
    val tomorrow = today + 1.days
    val regex = Regex("^\\d\\d")
    return when(regex.find(this)?.value?.toInt()) {
        yesterday.dayOfMonth -> Day.YESTERDAY
        today.dayOfMonth -> Day.TODAY
        tomorrow.dayOfMonth -> Day.TOMORROW
        else -> null
    }
}