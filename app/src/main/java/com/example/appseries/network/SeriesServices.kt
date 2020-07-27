package com.example.appseries.network

import android.util.Log
import com.example.appseries.adapter.RealtimeDataListener
import com.example.appseries.model.Serie
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

const val TAG = "SeriesService"
const val SERIES_COLLECTION_NAME = "series"

class SeriesServices {
    private val db = FirebaseFirestore.getInstance()
    private val settins = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true)
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
            "idSerie" to serie.idSerie,
            "nombre" to serie.nombre,
            "desc" to serie.desc,
            "imageId" to serie.imageId,
            "imageUrl" to serie.imageUrl,
            "fav" to serie.fav
        )

        db.collection(SERIES_COLLECTION_NAME).document(serie.idSerie).set(serieAdd)
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
            "idSerie" to serie.idSerie,
            "nombre" to serie.nombre,
            "desc" to serie.desc,
            "imageId" to serie.imageId,
            "imageUrl" to serie.imageUrl,
            "fav" to serie.fav
        )

        Log.d("SeriesServices", "id ${serie.getSerieDocumentId()}")
        Log.d("SeriesServices", "nombre ${serie.nombre}")
        Log.d("SeriesServices", "desc ${serie.desc}")
        Log.d("SeriesServices", "imageid ${serie.getSerieImageId()}")
        Log.d("SeriesServices", "imageurl ${serie.imageUrl}")
        Log.d("SeriesServices", "fav ${serie.fav}")

        db.collection(SERIES_COLLECTION_NAME).document(serie.idSerie)
            .update(serieEdit)
    }

    fun deleteSerie(serie:Serie) {
        db.collection(SERIES_COLLECTION_NAME).document(serie.idSerie)
            .delete()
    }
}