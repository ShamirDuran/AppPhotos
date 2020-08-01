package com.example.appseries.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.appseries.R
import com.example.appseries.model.Serie
import com.example.appseries.network.SeriesServices
import kotlinx.android.synthetic.main.fragment_edit_serie_dialog.*

class EditSerieDialogFragment : DialogFragment() {

    private val db = SeriesServices()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_serie_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarEditSerie.navigationIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_close)
        toolbarEditSerie.setNavigationOnClickListener{
            dismiss()
        }

        val serie = arguments?.getSerializable("serie") as Serie

        tieTituloSerie.setText(serie.nombre)
        tieDescSerie.setText(serie.desc)
        if (serie.fav) cbFavSerieEdit.isChecked = true

        btSaveEditSerie.setOnClickListener {

            if (tieTituloSerie.text.toString() != "" && tieDescSerie.text.toString() != ""){
                serie.nombre = tieTituloSerie.text.toString()
                serie.desc = tieDescSerie.text.toString()
                serie.fav = cbFavSerieEdit.isChecked
                db.updateSeries(serie)
                dismiss()
            } else {
                Toast.makeText(context, "No pueden haber campos vacios", Toast.LENGTH_SHORT).show()
            }
        }
    }
}