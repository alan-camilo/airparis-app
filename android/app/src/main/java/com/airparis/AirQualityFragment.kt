package com.airparis

import airparis.data.AirQualityCoordinator
import airparis.data.AirQualityState
import airparis.data.AirQualityViewModel
import airparis.data.http.model.util.Day
import android.content.Context
import android.os.Bundle
import android.view.View
import com.airparis.databinding.AirQualityFragmentBinding

class AirQualityFragment :
    BaseFragment<AirQualityCoordinator, AirQualityState, AirQualityViewModel, AirQualityFragmentBinding>(),
    AirQualityCoordinator {

    override fun onAttach(context: Context) {
        initialize(R.layout.air_quality_fragment, BR.actions, BR.state, AirQualityViewModel())
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.fetchDayIndex(Day.TODAY)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun showAirQuality(day: Day) {
        TODO("Not yet implemented")
    }
}