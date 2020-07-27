package com.example.appseries.model

import java.io.Serializable

class Serie(
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