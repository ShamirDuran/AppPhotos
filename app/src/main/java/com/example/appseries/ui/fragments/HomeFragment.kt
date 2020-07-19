package com.example.appseries.ui.fragments

import android.os.Bundle
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

        rvHome.layoutManager = GridLayoutManager(context, 2)
        rvHome.adapter = seriesAdapter

        observeSeriesViewModel()
    }

    fun observeSeriesViewModel() {
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

        val newserie = Serie(
            "Shingeki no kyojin",
            "La historia nos traslada a un mundo en el que la humanidad estuvo a punto de ser exterminada " +
                    "cientos de años atrás por los gigantes. Los gigantes son enormes, parecen no ser inteligentes" +
                    " y devoran seres humanos. Lo peor es que parece que lo hacen por placer y no por alimentarse. " +
                    "Una pequeña parte de la humanidad ha conseguido sobrevivir protegiéndose en una ciudad con unos " +
                    "altísimos muros, más altos que el mayor de los gigantes. La ciudad vive su vida tranquila, y hace" +
                    " más de 100 años que ningún gigante aparece por allí. Eren y su hermana adoptiva Mikasa son " +
                    "todavía unos adolescentes cuando ven algo horroroso: un gigante mucho mayor que todos los que " +
                    "la humanidad había conocido hasta el momento está destruyendo los muros de la ciudad. No pasa mucho" +
                    " tiempo hasta que los gigantes entran por el hueco abierto en el muro y comienzan a devorar a la gente.",
            "https://www.latercera.com/resizer/X8GDI9YjD0qVHEMmPrGaEnGEYz0=/900x600/smart/arc-anglerfish-arc2-prod-copesa.s3.amazonaws.com/public/CXE25YCTW5BWZFU2RV5AFJVZKE.jpg",
            true
        )


    }
}