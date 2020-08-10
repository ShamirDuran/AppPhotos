package com.example.appseries.model

import com.google.firebase.Timestamp
import java.io.Serializable

class Serie(
    var userId: String = "",
    var idSerie: String = "",
    var nombre: String = "",
    var desc: String = "",
    var imageId: String = "",
    var imageUrl: String = "",
    var fav: Boolean = false
) : Serializable {

    fun getSerieDocumentId(): String {
        return idSerie
    }

    fun getSerieImageId(): String {
        return imageId
    }
}