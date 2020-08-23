package com.example.appseries.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appseries.data.UserSingleton
import com.example.appseries.model.Serie
import com.example.appseries.model.User
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class HomeViewModel : ViewModel() {
    private val db = SeriesServices()
    private val listHelp = ArrayList<Serie>()
    private val listSeriesHome = MutableLiveData<List<Serie>>()

    init {
        getSeriesFollow()
    }

    private fun getSeriesFollow() {
        var user = db.getDataUser(object : Callback<User> {
            override fun onSuccess(result: User?) {
                result?.let { user ->
                    user.follow?.let {
                        getPhotos(it)
                    }
                }
            }

            override fun onFailed(exception: Exception) {
                Log.w("Error HomeVM", "No se consiguio info user logueado", exception)
            }

        })
    }

    private fun setListSeriesHome(data: ArrayList<Serie>) {
        this.listSeriesHome.value = data
    }

    private fun addArrayHelp(data: List<Serie>) {
        this.listHelp.addAll(data)
        Log.d("Mensaje", "contar")

        var order = listHelp.sortedWith(compareBy<Serie> { it.day }.thenBy { it.hour }).reversed()
        for (serie in order) {
            Log.d("Mensaje", "${serie.day}  ${serie.hour}")
        }
        this.listSeriesHome.value = order
    }

    fun getLiveDataSeriesHome(): LiveData<List<Serie>> {
        return this.listSeriesHome
    }

    private fun getPhotos(it: ArrayList<String>) {
        for (user_follow in it) {
            db.getSeries(object : Callback<List<Serie>> {
                override fun onSuccess(result: List<Serie>?) {
                    if (result != null) {
                        Log.d(
                            "SeriesService",
                            "Tamaño de una traida ${result.size}"
                        )
                        addArrayHelp(result)
                    }
                }

                override fun onFailed(exception: Exception) {
                    Log.w(
                        "Error HomeVM",
                        "No se obtuvo la lsita de seguidores para user logueado",
                        exception
                    )
                }

            }, user_follow)
        }
    }
}