package com.example.appseries.ui.fragments


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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

    private val db = SeriesServices()
    private val storageServ = StorageServices()
    private var selectedPhotoUri: Uri? = null
    private var fbimagenUrl: String = ""

    private val filename = UUID.randomUUID().toString()
    private val idSerie = UUID.randomUUID().toString()

    override fun onStart() {
        super.onStart()
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

        btAddSerie.setOnClickListener {
            var isFav = false
            if (cbFavSerie.isChecked) isFav = true

            val serie = Serie(
                idSerie,
                "s1",
                "s2",
                filename,
                "",
                isFav
            )
            storageServ.uploadImageToFirebase(filename, selectedPhotoUri, serie)

//            val serie = Serie(
//                idSerie,
//                tiNombreSerie.editText?.text.toString(),
//                tiDescripcionSerie.editText?.text.toString(),
//                filename,
//                storageServ.uploadImageToFirebase(filename,selectedPhotoUri),
//                isFav
//            )
            dismiss()
        }

        btSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

//        btUploadImage.setOnClickListener {
//            storageServ.uploadImageToFirebase(filename,selectedPhotoUri, object: RealtimeDataListener<String> {
//                override fun onDataChange(updatedData: String) {
//                    Log.d("StorageServices", "Url de la imagen $updatedData")
//                }
//
//                override fun onError(exception: Exception) {
//                    Log.w("StorageServices", "Error url imagne", exception)
//                }
//            })
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            ivSelectImage.setImageURI(selectedPhotoUri)
        }
    }
}
