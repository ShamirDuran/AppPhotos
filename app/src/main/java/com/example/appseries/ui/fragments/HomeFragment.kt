package com.example.appseries.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
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
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), PostListener {

    private lateinit var seriesAdapter: HomeAdapter
    private lateinit var homeViewModel: HomeViewModel

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

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        seriesAdapter = HomeAdapter(this)

        rvHome.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvHome.adapter = seriesAdapter

        fbAddSerie.setOnClickListener {
            onClickAddSerie()
        }

        observeSeriesViewModel()
    }

    private fun observeSeriesViewModel() {
        homeViewModel.getLiveDataSeriesHome()
            .observe(viewLifecycleOwner, Observer<List<Serie>> { series ->
                series.let {
                    seriesAdapter.updateListSeries(it)
                }
            })

        homeViewModel.getLiveDataUsers()
            .observe(viewLifecycleOwner, Observer<List<User>> { users ->
                users.let {
                    seriesAdapter.updateListUser(it)
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