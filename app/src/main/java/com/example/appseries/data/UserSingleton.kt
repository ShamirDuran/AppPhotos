package com.example.appseries.data

class UserSingleton {
    var userId = ""
    var nombre = ""
    var follow: ArrayList<String>? = ArrayList<String>()

    //    var followers: ArrayList<String>? = ArrayList<String>()
    var photosUploaded: Int = 0

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