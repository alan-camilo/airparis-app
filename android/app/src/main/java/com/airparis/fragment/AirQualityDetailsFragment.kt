package com.airparis.fragment

import airparis.data.AirQualityCoordinator
import airparis.data.AirQualityState
import airparis.data.AirQualityViewModel
import airparis.data.http.model.util.Day
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.airparis.BR
import com.airparis.R
import com.airparis.databinding.FragmentAirQualityDetailsBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_air_quality_details.*

const val POSITION_ARG = "position"

/**
 * A simple [Fragment] subclass.
 * Use the [AirQualityDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AirQualityDetailsFragment :
    BaseFragment<AirQualityCoordinator, AirQualityState, AirQualityViewModel, FragmentAirQualityDetailsBinding>(),
    AirQualityCoordinator {

    private var day: Day? = null

    override fun onAttach(context: Context) {
        initialize(
            R.layout.fragment_air_quality_details,
            AirQualityViewModel()
        )
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val position = it.getInt(POSITION_ARG)
            day = when (position) {
                0 -> Day.YESTERDAY
                1 -> Day.TODAY
                2 -> Day.TOMORROW
                else -> null
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        day?.let {
            viewModel.fetchDayIndex(it)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStateChange(state: AirQualityState) {
        state.dayIndexMap[day]?.global?.let {
            it.url_carte?.let { url ->
                Picasso.get().load(url).into(map_iv)
            }
            it.indice?.let { index ->
                day_index.text = index.toString()
            }
        }
        state.dayIndexMap[day]?.url_carte?.let { url ->
            Picasso.get().load(url).into(map_iv);
        }
        super.onStateChange(state)
    }

    override fun showAirQuality(day: Day) {
        TODO("Not yet implemented")
    }
}
