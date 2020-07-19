package com.example.appseries.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appseries.R
import com.example.appseries.adapter.FavoritesAdapter
import com.example.appseries.model.Serie
import com.example.appseries.viewmodel.SeriesViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*

/**
 * A simple [Fragment] subclass.
 * Use the [FavoritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritesFragment : Fragment() {

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
        favoritesAdapter = FavoritesAdapter()

        rvFav.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            adapter = favoritesAdapter
        }

        obserceViewModel()
    }

    private fun obserceViewModel() {
        seriesViewModel.getLiveDataSeriesFav().observe(viewLifecycleOwner, Observer<List<Serie>> { series ->
            series.let {
                favoritesAdapter.updateSeriesFav(series)
            }

            if (series.size != 0){
                rvFav.visibility = View.VISIBLE
                tvHayAlgo.visibility=View.INVISIBLE
            } else {
                rvFav.visibility = View.INVISIBLE
                tvHayAlgo.visibility=View.VISIBLE
            }
        })
    }
}