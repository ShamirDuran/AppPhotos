package com.example.appseries.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.appseries.R
import com.example.appseries.data.MessageFactory
import com.example.appseries.data.UserSingleton
import com.example.appseries.model.Serie
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices
import com.example.appseries.network.StorageServices
import com.example.appseries.viewmodel.SerieDetaillViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_series_detail_dialog.*
import java.lang.Exception

class SeriesDetailDialogFragment : DialogFragment() {

    private lateinit var serieDetaillViewModel: SerieDetaillViewModel
    private val db = SeriesServices()
    private val storageServ = StorageServices()
    private var isFav: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_series_detail_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serie = arguments?.getSerializable("serie") as Serie

        // Se comprueba si el post esta agregado como favorito
        isFav = UserSingleton.getInstance().seriesFav.contains(serie.idSerie)

        changeIconButtonFav()

        serieDetaillViewModel = ViewModelProvider(this).get(SerieDetaillViewModel::class.java)
        serieDetaillViewModel.setDataSerie(serie)

        if (serie.imageUrl != "") {
            Picasso.get().load(serie.imageUrl).into(ivImagenSerieDialog)
        }

        btDeleteSerie.setOnClickListener {
            val dialogFactory = MessageFactory()

            context?.let {
                val adviceDialog =
                    dialogFactory.getDialog(it, "typeAdvice", object : Callback<Boolean> {
                        override fun onSuccess(result: Boolean?) {
                            if (result == true) {
                                storageServ.deleteImageSerie(serie)
                                dismiss()
                            }
                        }

                        override fun onFailed(exception: Exception) {}
                    })
                adviceDialog.show()
            }
        }

        btCloseSerieDetail.setOnClickListener {
            dismiss()
        }

        btEditSerie.setOnClickListener {
            onEditSerieClicked(serie)
        }

        btAddFav.setOnClickListener {
            onAddFavClicked(serie)
            isFav = !isFav
            changeIconButtonFav()
        }

        observeSerieDetailViewModel()
        serieDetaillViewModel.suscribeToChanges()
    }


    private fun changeIconButtonFav() {
        if (isFav) {
            btAddFav.setImageResource(R.drawable.ic_fav)
        } else {
            btAddFav.setImageResource(R.drawable.ic_fav_border)
        }
    }

    private fun onAddFavClicked(serie: Serie) {
        db.updateFavorite(serie.idSerie, isFav)
    }

    private fun onEditSerieClicked(serie: Serie) {
        val bundle = bundleOf("serie" to serie)
        findNavController().navigate(R.id.editSerieDialogFragment, bundle)
    }

    private fun observeSerieDetailViewModel() {
        serieDetaillViewModel.getLivedataSerie()
            .observe(viewLifecycleOwner, Observer<Serie> { serie ->
                serie.let {
                    tvNombreSerieDialog.text = serie.nombre
                    tvDescSerieDialog.text = serie.desc
                }
            })
    }
}