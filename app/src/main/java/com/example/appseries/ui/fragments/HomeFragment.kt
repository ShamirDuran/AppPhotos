package com.example.appseries.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appseries.R
import com.example.appseries.adapter.HomeAdapter
import com.example.appseries.adapter.HomeListener
import com.example.appseries.data.UserSingleton
import com.example.appseries.model.Serie
import com.example.appseries.viewmodel.SeriesViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), HomeListener {

    private lateinit var seriesAdapter: HomeAdapter
    private lateinit var seriesViewModel: SeriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seriesViewModel = ViewModelProvider(this).get(SeriesViewModel::class.java)
        seriesAdapter = HomeAdapter(this)

        rvHome.layoutManager = GridLayoutManager(context, 1)
        rvHome.adapter = seriesAdapter

        fbAddSerie.setOnClickListener {
            onClickAddSerie()
        }

        observeSeriesViewModel()
        seriesViewModel.suscribeToChanges()

    }

    private fun observeSeriesViewModel() {
        seriesViewModel.getLiveDataListSeries()
            .observe(viewLifecycleOwner, Observer<List<Serie>> { series ->
                series.let {
                    seriesAdapter.updateListSeries(series)
                }
            })
    }

    override fun onSerieClicked(serie: Serie, position: Int) {
        val bundle = bundleOf("serie" to serie)
        findNavController().navigate(R.id.serieDetailFragmentDialog, bundle)
    }

    fun onClickAddSerie() {
        findNavController().navigate(R.id.addSerieDialogFragment)
    }
}