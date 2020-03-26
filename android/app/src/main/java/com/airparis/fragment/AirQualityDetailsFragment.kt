package com.airparis.fragment

import airparis.data.AirQualityCoordinator
import airparis.data.AirQualityState
import airparis.data.AirQualityViewModel
import airparis.data.http.model.util.Day
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.airparis.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_air_quality_details.*

const val POSITION_ARG = "position"

/**
 * A simple [Fragment] subclass.
 * Use the [AirQualityDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AirQualityDetailsFragment :
    BaseFragment<AirQualityCoordinator, AirQualityState, AirQualityViewModel>(),
    AirQualityCoordinator, Refresh {

    private var day: Day? = null
    private var position = -1

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
            position = it.getInt(POSITION_ARG)
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
            viewModel.fetchPollutionEpisode()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        (parentFragment as CollectionAirQualityFragment).setCurrentPage(position, this)
    }

    override fun onStateChange(state: AirQualityState) {
        if (state.dayIndexMap[day] != null && state.pollutionEpisodeList.isNotEmpty()) {
            displayValues(state)
        } else {
            displayLoading()
        }
        Log.d(AirQualityDetailsFragment::class.simpleName, state.pollutionEpisodeList.toString())
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

    private fun displayValues(state: AirQualityState) {
        progress.visibility = View.GONE
        global_index_tv.visibility = View.VISIBLE
        map_iv.visibility = View.VISIBLE
        pm10_index_tv.visibility = View.VISIBLE
        no2_index_tv.visibility = View.VISIBLE
        o3_index_tv.visibility = View.VISIBLE
        pollution_advice_tv.visibility = View.VISIBLE
        val dayIndex = state.dayIndexMap[day]!!
        dayIndex.global?.let {
            it.url_carte?.let { url ->
                Picasso.get().load(url).into(map_iv)
            }
            it.indice?.let { index ->
                global_index_tv.text = getString(R.string.global_index, index)
            }
        }
        dayIndex.url_carte?.let { url ->
            Picasso.get().load(url).into(map_iv)
        }
        dayIndex.pm10?.indice?.let {
            pm10_index_tv.text = getString(R.string.pm10_index, it)
        }
        dayIndex.no2?.indice?.let {
            no2_index_tv.text = getString(R.string.no2_index, it)
        }
        dayIndex.o3?.indice?.let {
            o3_index_tv.text = getString(R.string.o3_index, it)
        }
        val pollutionEpisode = state.pollutionEpisodeList.firstOrNull { episode ->
            episode.date == day?.value
        }
        if (pollutionEpisode?.detail == null) {
            pollution_advice_tv.visibility = View.GONE
        } else {
            pollution_advice_tv.visibility = View.VISIBLE
            pollution_advice_tv.text = pollutionEpisode.detail
        }
        super.onStateChange(state)
    }

    override fun showAirQuality(day: Day) {
        TODO("Not yet implemented")
    }

    override fun refresh() {
        Log.d(AirQualityDetailsFragment::class.simpleName, "refresh() day=$day")
        day?.let {
            displayLoading()
            viewModel.fetchDayIndex(it)
            viewModel.fetchPollutionEpisode()
        }
    }
}
