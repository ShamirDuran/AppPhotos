package com.example.appseries.data

import com.example.appseries.model.Serie

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