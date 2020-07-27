package com.example.appseries.ui.fragments


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.appseries.R
import com.example.appseries.model.Serie
import com.example.appseries.network.SeriesServices
import com.example.appseries.network.StorageServices

import kotlinx.android.synthetic.main.fragment_add_serie_dialog.*
import java.util.*

class AddSerieDialogFragment : DialogFragment() {

    val db = SeriesServices()
    val storageServ = StorageServices()
    var selectedPhotoUri: Uri? = null

    override fun onStart() {
        super.onStart()
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_serie_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarAddSerieDialogFragment.navigationIcon =
            ContextCompat.getDrawable(view.context, R.drawable.ic_close)
        toolbarAddSerieDialogFragment.setNavigationOnClickListener {
            dismiss()
        }

        val filename = UUID.randomUUID().toString()

        btAddSerie.setOnClickListener {
            var isFav: Boolean = false
            if (cbFavSerie.isChecked) isFav = true

            val serie = Serie(
                tiNombreSerie.editText?.text.toString(),
                tiDescripcionSerie.editText?.text.toString(),
                tiImageUrlSerie.editText?.text.toString(),
                isFav
            )

            db.addSerie(serie)
            dismiss()
        }

        btSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        btUploadImage.setOnClickListener {
            storageServ.uploadImageToFirebase(filename, selectedPhotoUri)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            ivSelectImage.setImageURI(selectedPhotoUri)
        }
    }
}