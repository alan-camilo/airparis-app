package fr.parisrespire.util

import android.content.Context
import android.content.res.Resources
import android.os.Build
import fr.parisrespire.R

class FragmentUtil(val resources: Resources) {

    fun getColorFromIndex(index: Int?): Int {
        if (index == null) return R.color.black_900
        return when {
            index in 0..24 -> R.color.very_good
            index in 25..49 -> R.color.good
            index in 50..74 -> R.color.mediocre
            index in 75..99 -> R.color.bad
            index > 99 -> R.color.very_bad
            else -> R.color.black_900
        }
    }

    fun getColorResFromIndex(index: Int?): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (index == null) return resources.getColor(R.color.black_900, null)
            return when {
                index in 0..24 -> resources.getColor(R.color.very_good, null)
                index in 25..49 -> resources.getColor(R.color.good, null)
                index in 50..74 -> resources.getColor(R.color.mediocre, null)
                index in 75..99 -> resources.getColor(R.color.bad, null)
                index > 99 -> resources.getColor(R.color.very_bad, null)
                else -> resources.getColor(R.color.black_900, null)
            }
        } else {
            if (index == null) return resources.getColor(R.color.black_900)
            return when {
                index in 0..24 -> resources.getColor(R.color.very_good)
                index in 25..49 -> resources.getColor(R.color.good)
                index in 50..74 -> resources.getColor(R.color.mediocre)
                index in 75..99 -> resources.getColor(R.color.bad)
                index > 99 -> resources.getColor(R.color.very_bad)
                else -> resources.getColor(R.color.black_900)
            }
        }
    }

    fun getQualityAdjectiveFromIndex(index: Int?, context: Context?): String? {
        if (index == null) return null
        return when {
            index in 0..24 -> context?.getString(R.string.very_low)
            index in 25..49 -> context?.getString(R.string.low)
            index in 50..74 -> context?.getString(R.string.mediocre)
            index in 75..99 -> context?.getString(R.string.high)
            index > 99 -> context?.getString(R.string.very_high)
            else -> null
        }
    }

}