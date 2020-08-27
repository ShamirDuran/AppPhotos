package com.example.appseries.network

import android.net.Uri
import android.util.Log
import com.example.appseries.model.Serie
import com.example.appseries.model.User
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception

class StorageServices {
    private val storageRef = FirebaseStorage.getInstance()
    private val db = SeriesServices()

    fun uploadImageToFirebase(filename: String, uri: Uri?, serie: Serie? = null) {

        var ref = storageRef.getReference("/images/$filename")

        if (serie == null) {
            ref = storageRef.getReference("/perfil/$filename")
        }

        ref.putFile(uri!!)
            .addOnSuccessListener {
                Log.d("StorageServices", "Succesfully uploaded image ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener { uri ->
                    //Log.d("StorageServices", "File location: ${it.toString()}")
                    if (serie != null) {
                        addSerie(serie, uri.toString())
                    } else {
                        // se esta subiendo una foto de perfil
                        deleteImageUser(filename, uri.toString())
                    }
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

    private fun deleteImageUser(filename:String, uri:String) {

        db.getDataUser(object : Callback<User>{
            override fun onSuccess(result: User?) {
                deleteFromStorage(result?.photoId, filename, uri)
            }

            override fun onFailed(exception: Exception) {
                Log.w("StorageServices", "Error al borrar antigua imagen", exception)
            }
        })
    }

    private fun deleteFromStorage(imageId: String?, filename: String, uri: String) {
        storageRef.getReference().child("perfil/$imageId")
            .delete()
            .addOnSuccessListener {
                Log.w("StorageServices", "Eliminada la imagen $imageId")
            }
            .addOnFailureListener {
                Log.w("StorageServices", "Error delete imagen $imageId", it)
            }
            .addOnCompleteListener {
                db.uploadPhotoUser(filename, uri)
            }
    }

    fun deleteImageSerie(serie: Serie) {
        val imageId = serie.imageId

        storageRef.getReference().child("images/$imageId")
            .delete()
            .addOnSuccessListener {
                Log.w("StorageServices", "Eliminada la imagen $imageId")
            }
            .addOnFailureListener {
                Log.w("StorageServices", "Error delete imagen $imageId", it)
            }

        db.deleteSerie(serie)
    }
}