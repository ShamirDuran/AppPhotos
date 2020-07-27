package com.example.appseries.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.appseries.R
import com.example.appseries.model.Serie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_series_detail_dialog.*


class SeriesDetailDialogFragment : DialogFragment() {

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

        toolbarSeriesDialog.navigationIcon =
            ContextCompat.getDrawable(view.context, R.drawable.ic_close)
        toolbarSeriesDialog.setNavigationOnClickListener {
            dismiss()
        }

        val serie = arguments?.getSerializable("serie") as Serie

        tvNombreSerieDialog.text = serie.nombre
        tvDescSerieDialog.text = serie.desc
        Picasso.get().load(serie.imageUrl).into(ivImagenSerieDialog)

        btEditSerie.setOnClickListener() {
            onEditSerieClicked(serie)
        }
    }

    fun onEditSerieClicked(serie: Serie) {
        val bundle = bundleOf("serie" to serie)
        findNavController().navigate(R.id.editSerieDialogFragment, bundle)
    }

//    override fun onStart() {
//        super.onStart()
//
//        dialog?.window?.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
//    }
}