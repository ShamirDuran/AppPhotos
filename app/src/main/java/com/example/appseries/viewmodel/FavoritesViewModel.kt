package com.example.appseries.viewmodel

import android.telecom.Call
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appseries.model.Serie
import com.example.appseries.model.User
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices
import java.lang.Exception

class FavoritesViewModel : ViewModel() {

    private val db = SeriesServices()
    private val listFavorites = MutableLiveData<List<Serie>>()

    init {
        getSeriesFav()
    }


    fun getSeriesFav() {
        db.getDataUser(object : Callback<User> {
            override fun onSuccess(result: User?) {
                result?.seriesFav.let { favorites ->
                    favorites?.let {
                        getSeries()
                    }
                }
            }

            override fun onFailed(exception: Exception) {
                Log.w("Error", "Error al obtener data user loguiado", exception)
            }
        })
    }

    private fun getSeries() {
        db.getSeriesFav(object : Callback<List<Serie>> {
            override fun onSuccess(result: List<Serie>?) {
                result?.let {
                    setListSeriesFav(it)
                }
            }

            override fun onFailed(exception: Exception) {
                Log.d("Error", "Error al obtener serie fav", exception)
            }
        })

    }

    fun getLiveDataSeriesFav(): LiveData<List<Serie>> {
        return this.listFavorites
    }

    fun setListSeriesFav(data: List<Serie>) {
        this.listFavorites.value = data
    }

}