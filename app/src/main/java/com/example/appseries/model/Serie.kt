package com.example.appseries.model

import com.google.firebase.Timestamp
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class Serie(
    var userId: String = "",
    var idSerie: String = "",
    var nombre: String = "",
    var desc: String = "",
    var imageId: String = "",
    var imageUrl: String = "",
    var fav: Boolean = false
) : Serializable {

    var hour:String = getCurrentDate()
    var day:String = getCurrentDay()

    private fun getCurrentDay(): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
        return sdf.format(Date())
    }

    private fun getCurrentDate():String {
        val sdf = SimpleDateFormat("hh-mm-ss", Locale.ENGLISH)
        return sdf.format(Date())
    }

    fun getSerieDocumentId(): String {
        return idSerie
    }

    fun getSerieImageId(): String {
        return imageId
    }
}