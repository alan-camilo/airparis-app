package fr.parisrespire.fragment

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.crashlytics.android.Crashlytics
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dev.icerock.moko.mvvm.MvvmFragment
import dev.icerock.moko.mvvm.createViewModelFactory
import fr.parisrespire.R
import fr.parisrespire.databinding.FragmentAirQualityDetailsBinding
import fr.parisrespire.mpp.data.AirQualityViewModel
import fr.parisrespire.mpp.data.CustomException
import fr.parisrespire.mpp.data.UIExceptionHandler
import fr.parisrespire.mpp.data.http.model.util.Day
import fr.parisrespire.util.getErrorMessage
import kotlinx.android.synthetic.main.fragment_air_quality_details.*

const val POSITION_ARG = "position"

class AirQualityDetailsFragment :
    MvvmFragment<FragmentAirQualityDetailsBinding, AirQualityViewModel>(),
    Refresh,
    UIExceptionHandler {

    override val layoutId = R.layout.fragment_air_quality_details
    override val viewModelClass: Class<AirQualityViewModel> = AirQualityViewModel::class.java
    override val viewModelVariableId: Int = dev.icerock.moko.mvvm.BR.viewModel

    private var day: Day? = null
    private var position = -1
    private var snackbar: Snackbar? = null

    // SlimChart
    private val stats = arrayOf(100F, 79.2F, 59.2F, 39.2F, 19.2F)
    private lateinit var colors: Array<Int>

    override fun viewModelFactory(): ViewModelProvider.Factory {
        return createViewModelFactory {
            AirQualityViewModel(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(POSITION_ARG)
            day = when (position) {
                0 -> Day.YESTERDAY
                1 -> Day.TODAY
                2 -> Day.TOMORROW
                else -> throw Exception("There must be only 3 instances of AirQualityDetailsFragment")
            }
        }
        colors = arrayOf(
            ContextCompat.getColor(context!!, R.color.very_bad),
            ContextCompat.getColor(context!!, R.color.bad),
            ContextCompat.getColor(context!!, R.color.mediocre),
            ContextCompat.getColor(context!!, R.color.good),
            ContextCompat.getColor(context!!, R.color.very_good)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        displayLoading()
        addObservers()
        viewModel.fetchDayIndex(day!!)
        viewModel.fetchPollutionEpisode(day!!)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        (parentFragment as CollectionAirQualityFragment).setCurrentPage(position, this)
    }

    private fun displayLoading() {
        progress.visibility = View.VISIBLE
        global_index_tv.visibility = View.GONE
        global_index_label.visibility = View.GONE
        slimChart.visibility = View.GONE
        map_iv.visibility = View.GONE
        pm10_index_tv.visibility = View.GONE
        no2_index_tv.visibility = View.GONE
        o3_index_tv.visibility = View.GONE
        pollution_advice_tv.visibility = View.GONE
        color_label_pm10.visibility = View.GONE
        color_label_no2.visibility = View.GONE
        color_label_o3.visibility = View.GONE
    }

    private fun displayViews() {
        // textviews
        global_index_label.visibility = View.VISIBLE
        global_index_tv.visibility = View.VISIBLE
        pm10_index_tv.visibility = View.VISIBLE
        no2_index_tv.visibility = View.VISIBLE
        o3_index_tv.visibility = View.VISIBLE
        // color label
        color_label_pm10.visibility = View.VISIBLE
        color_label_no2.visibility = View.VISIBLE
        color_label_o3.visibility = View.VISIBLE
    }

    private fun addObservers() {
        viewModel.dayIndex.addObserver {
            if (it == null) {
                // picasso default image
            } else if (context != null) {
                map_iv.visibility = View.VISIBLE
                // show image "données disponibles au bulletin de 11h"
                if (it.url_carte != null) Picasso.get().load(it.url_carte).into(map_iv)
                if (it.global != null) {
                    // show pollution map
                    Picasso.get().load(it.global?.url_carte).into(map_iv)
                    displayViews()
                    // Global air quality
                    global_index_tv.text =
                    getQualityAdjectiveFromIndex(it.global?.indice)?.capitalize()
                    global_index_tv.setTextColor(getColorResFromIndex(it.global?.indice))
                    // SlimChart
                    it.global?.indice?.let { index ->
                        val mIndex = minOf(100F, index * 0.8F)
                        val filteredStats =
                            listOf(mIndex) + stats.filter { stat -> stat < mIndex }
                        slimChart.stats = filteredStats.toFloatArray()
                        slimChart.colors = colors.drop(5 - filteredStats.size).toIntArray()
                        slimChart.setStartAnimationDuration(1700)
                        slimChart.textColor = getColorFromIndex(index)
                        slimChart.playStartAnimation()
                        slimChart.visibility = View.VISIBLE
                    }
                    // color label
                    color_label_pm10.setImageDrawable(getColorDrawableFromIndex(it.pm10?.indice))
                    color_label_no2.setImageDrawable(getColorDrawableFromIndex(it.no2?.indice))
                    color_label_o3.setImageDrawable(getColorDrawableFromIndex(it.o3?.indice))
                }
                progress.visibility = View.GONE
            }
        }
        viewModel.pollutionEpisode.addObserver {
            if (it != null && context != null) {
                pollution_advice_tv.visibility = View.VISIBLE
            }
        }
    }

    override fun refresh() {
        Log.d(AirQualityDetailsFragment::class.simpleName, "refresh() day=$day")
        displayLoading()
        viewModel.fetchDayIndex(day!!)
        viewModel.fetchPollutionEpisode(day!!)
        snackbar?.dismiss()
    }

    override fun showError(exception: CustomException) {
        if (context == null) return
        progress.visibility = View.GONE
        snackbar = Snackbar.make(
            coordinator_layout,
            getErrorMessage(context!!, exception),
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar?.show()
        Crashlytics.logException(exception)
    }

    private fun getColorFromIndex(index: Int?): Int {
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

    private fun getColorResFromIndex(index: Int?): Int {
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

    private fun getQualityAdjectiveFromIndex(index: Int?): String? {
        if (index == null) return null
        return when {
            index in 0..24 -> context?.getString(R.string.very_good)
            index in 25..49 -> context?.getString(R.string.good)
            index in 50..74 -> context?.getString(R.string.mediocre)
            index in 75..99 -> context?.getString(R.string.bad)
            index > 99 -> context?.getString(R.string.very_bad)
            else -> null
        }
    }

    private fun getColorDrawableFromIndex(index: Int?): Drawable? {
        if (index == null) return null
        return when {
            index in 0..24 -> context?.getDrawable(R.drawable.two_color_circle_shape_very_good)
            index in 25..49 -> context?.getDrawable(R.drawable.two_color_circle_shape_good)
            index in 50..74 -> context?.getDrawable(R.drawable.two_color_circle_shape_mediocre)
            index in 75..99 -> context?.getDrawable(R.drawable.two_color_circle_shape_bad)
            index > 99 -> context?.getDrawable(R.drawable.two_color_circle_shape_very_bad)
            else -> null
        }
    }
}
