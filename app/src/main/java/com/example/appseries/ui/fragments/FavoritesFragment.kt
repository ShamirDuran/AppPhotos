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
        Log.d("Mensaje", "entrooo")
        favoritesViewModel.getLiveDataSeriesFav()
            .observe(viewLifecycleOwner, Observer<List<Serie>> { series ->
                series.let {
                    favoritesAdapter.updateSeriesFav(it)
                    loadingScreenFav.visibility = View.INVISIBLE
                }
                if (!series.isNullOrEmpty()) {
                    tvMensaje.visibility = View.INVISIBLE
                } else {
                    tvMensaje.visibility = View.VISIBLE
                    loadingScreenFav.visibility = View.INVISIBLE
                }
                loadingScreenFav.visibility = View.INVISIBLE
                Log.d("Mensaje", "entrooo2")
            })
    }

    override fun onPostClicked(serie: Serie, position: Int) {
        val bundle = bundleOf("serie" to serie)
        findNavController().navigate(R.id.serieDetailFragmentDialog, bundle)
    }
}