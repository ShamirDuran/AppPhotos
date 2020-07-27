package com.example.appseries.network

import android.util.Log
import com.example.appseries.adapter.RealtimeDataListener
import com.example.appseries.model.Serie
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

const val TAG = "SeriesService"
const val SERIES_COLLECTION_NAME = "series"

class SeriesServices {
    val db = FirebaseFirestore.getInstance()
    val settins = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true)
        .build() // Habilita la persistencia de datos para el offline

    init {
        db.firestoreSettings = settins
    }

    fun getSeries(callback: Callback<List<Serie>>?) {
        db.collection(SERIES_COLLECTION_NAME)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    val list = result.toObjects(Serie::class.java)
                    callback!!.onSuccess(list)
                    break
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Fallo al traer de firestore los datos", exception)
            }

    }

    fun getSeriesFav(callback: Callback<List<Serie>>?) {
        db.collection(SERIES_COLLECTION_NAME)
            .whereEqualTo("fav", true)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    val list = result.toObjects(Serie::class.java)
                    callback!!.onSuccess(list)
                    break
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error al traer los favoritos desde fire", exception)
            }
    }

    fun addSerie(serie: Serie) {

        val serieAdd = hashMapOf(
            "nombre" to serie.nombre,
            "imageUrl" to serie.imageUrl,
            "imageId" to serie.imageId,
            "desc" to serie.desc,
            "fav" to serie.fav
        )

        db.collection(SERIES_COLLECTION_NAME).document(serie.getSerieDocumentId()).set(serieAdd)
            .addOnFailureListener {
                Log.d(TAG, "Agregado correctamente")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error al subir nueva serie", exception)
            }
    }

    fun listenForUpdates(listener: RealtimeDataListener<List<Serie>>) {
        val seriesReference = db.collection(SERIES_COLLECTION_NAME)

        seriesReference.addSnapshotListener { snapshot, error ->
            error?.let {
                listener.onError(it)
            }

            snapshot?.let {
                val lista: List<Serie> = snapshot.toObjects(Serie::class.java)
                listener.onDataChange(lista)
            }
        }

    }

    fun updateSeries(serie: Serie) {
        val serieEdit = hashMapOf(
            "nombre" to serie.nombre,
            "imageUrl" to serie.imageUrl,
            "imageId" to serie.imageId,
            "desc" to serie.desc,
            "fav" to serie.fav
        )
        db.collection(SERIES_COLLECTION_NAME).document(serie.getSerieDocumentId())
            .update(serieEdit)
    }

    fun deleteSerie(serie:Serie) {
        db.collection(SERIES_COLLECTION_NAME).document(serie.getSerieDocumentId())
            .delete()
    }
}