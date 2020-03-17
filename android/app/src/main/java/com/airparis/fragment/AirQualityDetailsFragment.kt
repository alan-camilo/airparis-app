package com.airparis

import airparis.data.AirQualityCoordinator
import airparis.data.AirQualityState
import airparis.data.AirQualityViewModel
import airparis.data.http.model.util.Day
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airparis.databinding.FragmentAirQualityDetailsBinding
import com.airparis.fragment.BaseFragment
import com.squareup.picasso.Picasso

const val DAY_ARG = "DAY"

/**
 * A simple [Fragment] subclass.
 * Use the [AirQualityDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AirQualityDetailsFragment :
    BaseFragment<AirQualityCoordinator, AirQualityState, AirQualityViewModel, FragmentAirQualityDetailsBinding>(),
    AirQualityCoordinator {

    private var day: String? = null

    override fun onAttach(context: Context) {
        initialize(
            R.layout.fragment_air_quality_collection,
            BR.actions,
            BR.state, AirQualityViewModel())
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            day = it.getString(DAY_ARG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_air_quality_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.fetchDayIndex(Day.TODAY)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStateChange(state: AirQualityState) {
        state.dayIndexMap[Day.TODAY]?.global?.url_carte?.let {
            Picasso.get().load(it).into(map_iv);
        }
        super.onStateChange(state)
    }
}
