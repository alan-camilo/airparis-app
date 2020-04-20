package fr.parisrespire.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
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

    override fun viewModelFactory(): ViewModelProvider.Factory {
        return createViewModelFactory {
            AirQualityViewModel(
                this
            )
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
        map_iv.visibility = View.GONE
        pm10_index_tv.visibility = View.GONE
        no2_index_tv.visibility = View.GONE
        o3_index_tv.visibility = View.GONE
        pollution_advice_tv.visibility = View.GONE
    }

    private fun addObservers() {
        viewModel.dayIndex.addObserver {
            if (it == null) {
                // picasso default image
            } else if (context != null) {
                Picasso.get().load(it.global?.url_carte).into(map_iv)
                // make textviews visible
                global_index_tv.visibility = View.VISIBLE
                progress.visibility = View.GONE
                global_index_tv.visibility = View.VISIBLE
                map_iv.visibility = View.VISIBLE
                pm10_index_tv.visibility = View.VISIBLE
                no2_index_tv.visibility = View.VISIBLE
                o3_index_tv.visibility = View.VISIBLE
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
        Crashlytics.logException(exception.throwable)
    }
}
