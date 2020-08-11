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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appseries.R
import com.example.appseries.adapter.FavoritesAdapter
import com.example.appseries.adapter.PostListener
import com.example.appseries.model.Serie
import com.example.appseries.viewmodel.SeriesViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : Fragment(), PostListener {

    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var seriesViewModel: SeriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seriesViewModel = ViewModelProvider(this).get(SeriesViewModel::class.java)
        favoritesAdapter = FavoritesAdapter(this)

        rvFav.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = favoritesAdapter
        }

        obserceViewModel()
    }

    private fun obserceViewModel() {
        seriesViewModel.getLiveDataSeriesFav().observe(viewLifecycleOwner, Observer<List<Serie>> { series ->
            series.let {
                favoritesAdapter.updateSeriesFav(it)
            }

            // Muestra un mensaje avisando que no hya nada
            if (series.size != 0){
                rvFav.visibility = View.VISIBLE
                tvHayAlgo.visibility=View.INVISIBLE
            } else {
                rvFav.visibility = View.INVISIBLE
                tvHayAlgo.visibility=View.VISIBLE
            }
        })
    }

    override fun onPostClicked(serie: Serie, position: Int) {
        val bundle = bundleOf("serie" to serie)
        findNavController().navigate(R.id.serieDetailFragmentDialog, bundle)
    }
}