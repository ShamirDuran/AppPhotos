package com.example.appseries.network

import android.util.Log
import com.example.appseries.model.Serie
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

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
                    Log.d("abcd: ", list.javaClass.name)
                    callback!!.onSuccess(list)
                    break
                }
            }
            .addOnFailureListener { exception ->
                Log.w("SERVICES_FIRESTORE: ", "Fallo al traer de firestore los datos", exception)
            }

    }

    fun getSeriesFav(callback: Callback<List<Serie>>?) {
        db.collection(SERIES_COLLECTION_NAME)
            .whereEqualTo("fav", true)
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    Log.d("Datos fav desde fir: ", "${document.id} => ${document.data}")
                }

                for (doc in result) {
                    val list = result.toObjects(Serie::class.java)
                    callback!!.onSuccess(list)
                    break
                }
            }
            .addOnFailureListener { exception ->
                Log.w("SERVICES_FIRESTORE", "Error al traer los favoritos desde fire", exception)
            }
    }
}