package com.example.appseries.network

import android.util.Log
import com.example.appseries.adapter.RealtimeDataListener
import com.example.appseries.data.UserSingleton
import com.example.appseries.model.Comment
import com.example.appseries.model.Serie
import com.example.appseries.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import java.lang.Exception

const val TAG = "SeriesService"
const val USER_COLLECTION_NAME = "users"
const val SERIES_COLLECTION_NAME = "series"
const val COMMENTS_COLLECTION_NAME = "comments"

class SeriesServices {
    private val db = FirebaseFirestore.getInstance()
    private val userUID = FirebaseAuth.getInstance().currentUser?.uid
    private val settins = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true)
        .build() // Habilita la persistencia de datos para el offline

    init {
        db.firestoreSettings = settins
    }

    fun getAllSeries(callback: Callback<List<Serie>>?) {
        db.collection(SERIES_COLLECTION_NAME)
            .orderBy("day", Query.Direction.DESCENDING)
            .orderBy("hour", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { result ->
                result?.let {
                    val list = it.toObjects(Serie::class.java)
                    callback?.onSuccess(list)
                }
            }
            .addOnFailureListener {exception ->
                callback?.onFailed(exception)
            }
    }

    fun getMySeries(callback: Callback<List<Serie>>?, userID: String = userUID!!) {
        db.collection(SERIES_COLLECTION_NAME)
            .whereEqualTo("userId", userID)
            .orderBy("day", Query.Direction.DESCENDING)
            .orderBy("hour", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    Log.d(TAG, "UserID : $userID")
                    val list = result.toObjects(Serie::class.java)
                    callback!!.onSuccess(list)
                    break
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Fallo al traer de firestore los datos", exception)
            }
    }

    fun getFollowSeries(callback: Callback<List<Serie>>?) {
        if (!UserSingleton.getInstance().follow.isNullOrEmpty()) {
            db.collection(SERIES_COLLECTION_NAME)
                .whereIn("userId", UserSingleton.getInstance().follow!!.toList())
                .orderBy("day", Query.Direction.DESCENDING)
                .orderBy("hour", Query.Direction.DESCENDING)
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
        } else {
            val list = listOf<Serie>()
            callback?.onSuccess(list)
        }
    }

    fun getSeriesFav(callback: Callback<List<Serie>>?) {
        if (!UserSingleton.getInstance().seriesFav.isNullOrEmpty()) {
            db.collection(SERIES_COLLECTION_NAME)
                .whereIn("idSerie", UserSingleton.getInstance().seriesFav!!.toList())
                .orderBy("day", Query.Direction.DESCENDING)
                .orderBy("hour", Query.Direction.DESCENDING)
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
        } else {
            val list = listOf<Serie>()
            callback?.onSuccess(list)
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
            .update("photosUploaded", FieldValue.increment(1))
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
                Log.w(TAG, "Error al traerDataUser")
            }
    }

    fun getDataFollow(callback: Callback<List<User>>?) {
        if (!UserSingleton.getInstance().follow.isNullOrEmpty()) {
            db.collection(USER_COLLECTION_NAME)
                .whereIn("userId", UserSingleton.getInstance().follow!!.toList())
                .get()
                .addOnSuccessListener { result ->
                    Log.d(TAG, "Traido correctamente")
                    val userData = result.toObjects(User::class.java)
                    callback?.onSuccess(userData)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error al traer usuario", exception)
                    callback?.onFailed(exception)
                }
        } else {
            val list = listOf<User>()
            callback?.onSuccess(list)
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

    fun getDataSerie(serie_id: String, callback: Callback<Serie>?) {
        db.collection(SERIES_COLLECTION_NAME).document(serie_id)
            .get()
            .addOnSuccessListener { result ->
                if (result.data != null) {
                    val serieData: Serie = result.toObject(Serie::class.java)!!
                    callback!!.onSuccess(serieData)
                } else {
                    deleteRefFav(serie_id)
                }
            }
            .addOnFailureListener { exception ->
                callback!!.onFailed(exception)
            }
    }

    fun listenForUpdates(listener: RealtimeDataListener<List<Serie>>, userID: String = userUID!!) {
        val ref = db.collection(SERIES_COLLECTION_NAME)
            .whereEqualTo("userId", userID)
            .orderBy("day", Query.Direction.DESCENDING)
            .orderBy("hour", Query.Direction.DESCENDING)
        ref.addSnapshotListener { snapshot, error ->
            error?.let {
                listener.onError(it)
            }

            snapshot?.let {
                val list: List<Serie> = snapshot.toObjects(Serie::class.java)
                listener.onDataChange(list)
            }
        }
    }

    fun listenForUpdateComments(post_id: String, listener: RealtimeDataListener<List<Comment>>) {
        val commentRef = db.collection(COMMENTS_COLLECTION_NAME)
            .whereEqualTo("post_id", post_id)
            .orderBy("date", Query.Direction.ASCENDING)

        commentRef.addSnapshotListener { snapshot, error ->
            error?.let {
                listener.onError(it)
            }

            snapshot?.let {
                val list = snapshot.toObjects(Comment::class.java)
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
            .update("photosUploaded", FieldValue.increment(-1))
    }

    fun updateFollow(friend: User, isFollow: Boolean) {

        if (isFollow) {
            // empezar a seguir
            friend.follow?.add(userUID!!)
            UserSingleton.getInstance().follow?.add(friend.userId)

            db.collection(USER_COLLECTION_NAME).document(userUID!!)
                .update("follow", FieldValue.arrayUnion(friend.userId))

            db.collection(USER_COLLECTION_NAME).document(friend.userId)
                .update("followers", FieldValue.arrayUnion(userUID))
        } else {
            // dejar de seguir
            friend.follow?.remove(UserSingleton.getInstance().userId)
            UserSingleton.getInstance().follow?.remove(friend.userId)

            db.collection(USER_COLLECTION_NAME).document(userUID!!)
                .update("follow", FieldValue.arrayRemove(friend.userId))

            db.collection(USER_COLLECTION_NAME).document(friend.userId)
                .update("followers", FieldValue.arrayRemove(userUID))
        }
    }

    fun updateFavorite(idSerie: String, isFav: Boolean) {
        if (UserSingleton.getInstance().seriesFav != null) {
            if (isFav) {
                // Esta agregado a favs, quiere quitarlo
                UserSingleton.getInstance().seriesFav!!.remove(idSerie)
                db.collection(USER_COLLECTION_NAME).document(userUID!!)
                    .update("seriesFav", UserSingleton.getInstance().seriesFav)
                    .addOnFailureListener { Log.w(TAG, "Error al quitar de fav", it) }
            } else {
                // No estaba agregado a fav, quiere agregarlo
                UserSingleton.getInstance().seriesFav!!.add(idSerie)
                db.collection(USER_COLLECTION_NAME).document(userUID!!)
                    .update("seriesFav", UserSingleton.getInstance().seriesFav)
                    .addOnFailureListener { Log.w(TAG, "Error al tratar de agregar a fav", it) }
            }
        }
    }

    private fun deleteRefFav(serieId: String) {
        db.collection(USER_COLLECTION_NAME).document(userUID!!)
            .update("seriesFav", FieldValue.arrayRemove(serieId))
            .addOnSuccessListener {
                Log.d("Mensaje", "Se eliminio serie fav inexistente")
            }
            .addOnFailureListener {
                Log.w("Mensaje", "Error al eliminar serie inexistente fav")
            }
    }

    fun addComment(comment: Comment, callback: Callback<Boolean>) {
        val comment = hashMapOf(
            "user_id" to userUID,
            "post_id" to comment.post_id,
            "content" to comment.content,
            "date" to comment.date
        )

        db.collection(COMMENTS_COLLECTION_NAME)
            .add(comment)
            .addOnSuccessListener {
                callback.onSuccess(true)
            }
            .addOnFailureListener {
                callback.onFailed(it)
            }
    }

    fun getComment(postId: String, callback: Callback<List<Comment>>?) {
        db.collection(COMMENTS_COLLECTION_NAME)
            .whereEqualTo("post_id", postId)
            .orderBy("date", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    val list = result.toObjects(Comment::class.java)
                    callback?.onSuccess(list)
                    break
                }
            }
            .addOnFailureListener {
                callback?.onFailed(it)
            }
    }

    fun uploadPhotoUser(photoId: String, url: String) {
        val userData = hashMapOf(
            "photo" to url,
            "photoId" to photoId
        )
        db.collection(USER_COLLECTION_NAME).document(userUID!!)
            .update(userData as Map<String, Any>)
            .addOnSuccessListener {
                Log.d("SeriesServices", "Foto perfil actualizada correctamente")
            }
            .addOnFailureListener {
                Log.w("SeriesServices", "Error al subir la foto de perfil", it)
            }
    }
}