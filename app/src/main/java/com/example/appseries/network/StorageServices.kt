package com.example.appseries.network

import android.net.Uri
import android.util.Log
import com.example.appseries.model.Serie
import com.google.firebase.storage.FirebaseStorage

class StorageServices {
    private val storageRef = FirebaseStorage.getInstance()
    private val db = SeriesServices()
    private var direc: String = ""

    fun uploadImageToFirebase(filename: String, uri: Uri?, serie:Serie) {
        val ref = storageRef.getReference("/images/$filename")
        ref.putFile(uri!!)
            .addOnSuccessListener {
                Log.d("StorageServices", "Succesfully uploaded image ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    //Log.d("StorageServices", "File location: ${it.toString()}")
                    addSerie(serie, it.toString())
                }

                ref.downloadUrl.addOnFailureListener {
                    Log.w("StorageServices", "Error get file location", it)
                }
            }
    }

    private fun addSerie(serie: Serie, url: String) {
        serie.imageUrl = url
        db.addSerie(serie)
    }

}