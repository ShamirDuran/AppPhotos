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
import com.example.appseries.viewmodel.FavoritesViewModel
import com.example.appseries.viewmodel.SeriesViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*
import java.lang.Exception
import java.util.*
import kotlin.concurrent.schedule

class FavoritesFragment : Fragment(), PostListener {

    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingScreenFav.visibility = View.VISIBLE

        favoritesViewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)
        favoritesAdapter = FavoritesAdapter(this)

        rvFav.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = favoritesAdapter
        }

        obserceViewModel()
    }

    private fun obserceViewModel() {
        favoritesViewModel.getLiveDataSeriesFav()
            .observe(viewLifecycleOwner, Observer<List<Serie>> { series ->
                series.let {
                    favoritesAdapter.updateSeriesFav(it)
                    Timer().schedule(500) {
                        try {
                            loadingScreenFav.visibility = View.INVISIBLE
                        } catch (e: Exception){}
                    }
                }
            })
    }

    override fun onPostClicked(serie: Serie, position: Int) {
        val bundle = bundleOf("serie" to serie)
        findNavController().navigate(R.id.serieDetailFragmentDialog, bundle)
    }
}