package com.example.appseries.model

import java.io.Serializable

class User(
    var userId: String = "",
    var nombre: String = "",
    var follow: ArrayList<String>? = ArrayList(),
    var followers: ArrayList<String>? = ArrayList(),
    var photosUploaded:Int = 0,
    var seriesFav:ArrayList<String> = ArrayList()
) : Serializable