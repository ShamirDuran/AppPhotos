package com.example.appseries.model

import java.io.Serializable

class User(
    var userId: String = "",
    var nombre: String = "",
    var follow: ArrayList<String>? = ArrayList<String>(),
    var followers: ArrayList<String>? = ArrayList<String>(),
    var photosUploaded:Int = 0
) : Serializable