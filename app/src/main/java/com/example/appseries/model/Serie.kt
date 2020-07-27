package com.example.appseries.model

import java.io.Serializable

class Serie(
    var nombre: String = "",
    var desc: String = "",
    var imageUrl: String = "",
    var fav: Boolean = false
) : Serializable {

    fun getSerieDocumentId(): String {
        return nombre
    }
}