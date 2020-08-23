package com.example.appseries.data

import android.util.Log
import com.example.appseries.model.Serie
import com.example.appseries.model.User
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices

class UserSingleton {
    var userId = ""
    var nombre = ""
    var follow: ArrayList<String>? = ArrayList()
    var followers: ArrayList<String>? = ArrayList()
    var photosUploaded: Int = 0
    var seriesFav:ArrayList<String> = ArrayList()

    companion object {
        private var instance: UserSingleton? = null

        fun getInstance(): UserSingleton {
            if (instance == null) {
                instance = UserSingleton()
            }
            return instance as UserSingleton
        }
    }
}