package com.example.appseries.network

import android.util.Log
import com.example.appseries.adapter.RealtimeDataListener
import com.example.appseries.data.UserSingleton
import com.example.appseries.model.Serie
import com.example.appseries.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import java.lang.Exception

const val TAG = "SeriesService"
const val USER_COLLECTION_NAME = "users"
const val SERIES_COLLECTION_NAME = "series"

class SeriesServices {
    private val db = FirebaseFirestore.getInstance()
    private val userUID = FirebaseAuth.getInstance().currentUser?.uid
    private val recentSeries = ArrayList<Serie>()
    private val settins = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true)
        .build() // Habilita la persistencia de datos para el offline

    init {
        db.firestoreSettings = settins
    }

    private fun setRecentSeries(data: List<Serie>) {
        this.recentSeries.addAll(data)
    }

    fun getRecentSeriesHome(listFollow: List<String>, callback: Callback<List<Serie>>?) {
        Log.d("lista", "listfollow: ${listFollow.size}")

//        listFollow.let { users ->
//                db.collection(SERIES_COLLECTION_NAME)
//                    .whereArrayContainsAny("userId", listFollow)
//                    .orderBy("day", Query.Direction.DESCENDING)
//                    .orderBy("hour", Query.Direction.DESCENDING)
//                    .limit(5)
//                    .get()
//                    .addOnSuccessListener { result ->
//                        val list: List<Serie> = result.toObjects(Serie::class.java)
//                        Log.d("Encontro", "Series: ${list.size}")
//                        setRecentSeries(list)
//                    }
//
//                    .addOnFailureListener {
//                        Log.w("Lista", "error", it)
//                        callback?.onFailed(it)
//                    }
//                Log.d("lista", "size follow img add: ${recentSeries.size}")
//            }
//            recentSeries.let { list ->
//                val send = list.sortedWith(compareByDescending<Serie> { it.hour }.thenBy { it.day })
//                callback?.onSuccess(send)
//
//        }
    }

    fun getSeries(callback: Callback<List<Serie>>?, userID: String = userUID!!) {
        db.collection(SERIES_COLLECTION_NAME)
            .whereEqualTo("userId", userID)
            .orderBy("day", Query.Direction.DESCENDING)
            .orderBy("hour", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    Log.d(TAG, "UserID : $userID")
                    val list = result.toObjects(Serie::class.java)
//                    list.sortedWith(compareByDescending<Serie> { it.hour }.thenBy { it.day })
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
            .whereEqualTo("userId", userUID)
            .whereEqualTo("fav", true)
            .orderBy("day", Query.Direction.DESCENDING)
            .orderBy("hour", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    val list = result.toObjects(Serie::class.java)
//                    list = list.sortedWith(compareByDescending<Serie> { it.hour }.thenBy { it.day })
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
            "userId" to userUID,
            "idSerie" to serie.idSerie,
            "nombre" to serie.nombre,
            "desc" to serie.desc,
            "imageId" to serie.imageId,
            "imageUrl" to serie.imageUrl,
            "fav" to serie.fav,
            "day" to serie.day,
            "hour" to serie.hour
        )

        UserSingleton.getInstance().photosUploaded += 1

        db.collection(SERIES_COLLECTION_NAME).document(serie.idSerie)
            .set(serieAdd)
            .addOnSuccessListener {
                Log.d(TAG, "Agregado correctamente")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error al subir nueva serie", exception)
            }

        db.collection(USER_COLLECTION_NAME).document(userUID!!)
            .update("photosUploaded", UserSingleton.getInstance().photosUploaded)
    }

    fun addUser(nombre: String, uid: String) {
        val userDetails = hashMapOf(
            "userId" to uid,
            "nombre" to nombre
        )

        db.collection(USER_COLLECTION_NAME).document(uid).set(userDetails)
            .addOnSuccessListener {
                Log.d(TAG, "Agregado correctamente")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error al crear usuario", exception)
            }
    }

    fun getDataUser(callback: Callback<User>?, userID: String = userUID!!) {
        db.collection(USER_COLLECTION_NAME).document(userID)
            .get()
            .addOnSuccessListener { result ->
                Log.d(TAG, "Traido correctamente")
                val userData: User = result.toObject(User::class.java)!!
                callback!!.onSuccess(userData)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error al traer usuario", exception)
                callback!!.onFailed(exception)
            }
    }

    fun getSearchUser(ref: String, callback: Callback<List<User>>?) {
        db.collection(USER_COLLECTION_NAME)
            .whereGreaterThanOrEqualTo("nombre", ref)
            .get()
            .addOnSuccessListener {
                val usersData = it.toObjects(User::class.java)
                callback?.onSuccess(usersData)
            }
            .addOnFailureListener {
                Log.w(TAG, "Error referencia usuarios: ", it)
                callback?.onFailed(it)
            }
    }

    fun getDataSerie(serie: Serie, callback: Callback<Serie>?) {
        db.collection(SERIES_COLLECTION_NAME).document(serie.idSerie)
            .get()
            .addOnSuccessListener { result ->
                val serieData: Serie = result.toObject(Serie::class.java)!!
                callback!!.onSuccess(serieData)
            }
            .addOnFailureListener { exception ->
                callback!!.onFailed(exception)
            }
    }

    fun listenForUpdates(listener: RealtimeDataListener<List<Serie>>, userID: String = userUID!!) {
        val seriesReference = db.collection(SERIES_COLLECTION_NAME)
            .whereEqualTo("userId", userID)
            .orderBy("day", Query.Direction.DESCENDING)
            .orderBy("hour", Query.Direction.DESCENDING)
        seriesReference.addSnapshotListener { snapshot, error ->
            error?.let {
                listener.onError(it)
            }

            snapshot?.let {
                val list: List<Serie> = snapshot.toObjects(Serie::class.java)
//                list.sortedWith(compareByDescending<Serie> { it.hour }.thenBy { it.day })
                listener.onDataChange(list)
            }
        }
    }

    fun listenForUpdatesUser(listener: RealtimeDataListener<User>) {
        val userReference = db.collection(USER_COLLECTION_NAME).document(userUID!!)
        userReference.addSnapshotListener { snapshot, e ->
            e?.let {
                listener.onError(it)
            }

            snapshot?.let {
                val user: User = snapshot.toObject(User::class.java)!!
                listener.onDataChange(user)
            }
        }
    }

    fun listenForUpdatesSerieDetail(serie: Serie, listener: RealtimeDataListener<Serie>) {
        val serieReference = db.collection(SERIES_COLLECTION_NAME).document(serie.idSerie)
        serieReference.addSnapshotListener { snapshot, e ->
            e?.let {
                listener.onError(e)
            }

            snapshot?.let {
                val serieData = snapshot.toObject(Serie::class.java)

                if (serieData != null) {
                    listener.onDataChange(serieData)
                }
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

        db.collection(SERIES_COLLECTION_NAME).document(serie.idSerie)
            .update(serieEdit)
    }

    fun deleteSerie(serie: Serie) {
        db.collection(SERIES_COLLECTION_NAME).document(serie.idSerie)
            .delete()

        UserSingleton.getInstance().photosUploaded -= 1
        if (UserSingleton.getInstance().photosUploaded < 0) {
            UserSingleton.getInstance().photosUploaded = 0
        }

        db.collection(USER_COLLECTION_NAME).document(userUID!!)
            .update("photosUploaded", UserSingleton.getInstance().photosUploaded)
    }

    fun updateFollow(friend: User, isFollow: Boolean) {

        if (isFollow) {
            // empezar a seguir
            friend.follow?.add(userUID!!)
            UserSingleton.getInstance().follow?.add(friend.userId)

            db.collection(USER_COLLECTION_NAME).document(userUID!!)
                .update("follow", UserSingleton.getInstance().follow)

            db.collection(USER_COLLECTION_NAME).document(friend.userId)
                .update("followers", friend.follow)
        } else {
            // dejar de seguir
            friend.follow?.remove(UserSingleton.getInstance().userId)
            UserSingleton.getInstance().follow?.remove(friend.userId)

            db.collection(USER_COLLECTION_NAME).document(userUID!!)
                .update("follow", UserSingleton.getInstance().follow)

            db.collection(USER_COLLECTION_NAME).document(friend.userId)
                .update("followers", friend.follow)
        }
    }
}