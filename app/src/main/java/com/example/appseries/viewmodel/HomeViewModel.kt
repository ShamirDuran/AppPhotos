package com.example.appseries.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appseries.model.Serie
import com.example.appseries.model.User
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices

class HomeViewModel : ViewModel() {
    private val db = SeriesServices()
    private val listSeriesHome = MutableLiveData<List<Serie>>()
    private val listUsers = MutableLiveData<List<User>>()

    init {
        getSeriesFriends()
    }

    fun getSeriesFriends() {
        db.getDataUser(object : Callback<User> {
            override fun onSuccess(result: User?) {
                result?.let { user ->
                    user.follow?.let {
                        getPhotos()
                    }
                }
            }

            override fun onFailed(exception: Exception) {
                Log.w("Error HomeVM", "No se consiguio info user logueado", exception)
            }
        })
    }

    fun getAllSeries() {
        db.getAllSeries(object :Callback<List<Serie>>{
            override fun onSuccess(result: List<Serie>?) {
                result?.let {
                    setListSeriesHome(it)
                }
            }

            override fun onFailed(exception: Exception) {
                Log.w("Error HomeVM", "Errro al traer todas los post", exception)
            }
        })
    }

    private fun setListSeriesHome(data: List<Serie>) {
        this.listSeriesHome.value = data
    }

    fun getLiveDataSeriesHome(): LiveData<List<Serie>> {
        return this.listSeriesHome
    }

    fun getLiveDataUsers(): LiveData<List<User>> {
        return this.listUsers
    }

    private fun getPhotos() {
        db.getFollowSeries(object : Callback<List<Serie>> {
            override fun onSuccess(result: List<Serie>?) {
                if (result != null) setListSeriesHome(result)
            }

            override fun onFailed(exception: Exception) {
                Log.w("Error HomeVM", "No se obtuvo series amgios", exception)
            }
        })

        // Obtenemos la info del los que seguimos
        db.getDataFollow(object : Callback<List<User>> {
            override fun onSuccess(result: List<User>?) {
                if (result != null) listUsers.value = result
            }

            override fun onFailed(exception: java.lang.Exception) {
                Log.w("Error HomeVM", "No se pudo obtener la data del follower")
            }
        })
    }
}


