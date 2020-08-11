package com.example.appseries.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appseries.R
import com.example.appseries.adapter.HomeAdapter
import com.example.appseries.adapter.PostListener
import com.example.appseries.data.UserSingleton
import com.example.appseries.model.Serie
import com.example.appseries.model.User
import com.example.appseries.network.Callback
import com.example.appseries.viewmodel.HomeViewModel
import com.example.appseries.viewmodel.SeriesViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception

class HomeFragment : Fragment(), PostListener {

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

        rvHome.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvHome.adapter = seriesAdapter

        fbAddSerie.setOnClickListener {
            onClickAddSerie()
        }

        observeSeriesViewModel()

    }

    private fun observeSeriesViewModel() {
        seriesViewModel.getLiveDataListSeries()
            .observe(viewLifecycleOwner, Observer<List<Serie>> { series ->
                series.let {
                    seriesAdapter.updateListSeries(series)
                }
            })
    }

    override fun onPostClicked(serie: Serie, position: Int) {
        val bundle = bundleOf("serie" to serie)
        findNavController().navigate(R.id.serieDetailFragmentDialog, bundle)
    }

    private fun onClickAddSerie() {
        findNavController().navigate(R.id.addSerieDialogFragment)
    }
}