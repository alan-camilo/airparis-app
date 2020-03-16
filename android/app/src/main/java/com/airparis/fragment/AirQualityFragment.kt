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
