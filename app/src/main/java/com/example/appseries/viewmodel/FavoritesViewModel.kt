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
    private val listHelp = ArrayList<Serie>();

    init {
        getSeriesFav()
    }

    private fun getSeriesFav() {

        db.getDataUser(object : Callback<User> {
            override fun onSuccess(result: User?) {
                result?.seriesFav.let { favorites ->
                    favorites?.let {
                        getSeries(it)
                    }
                }
            }

            override fun onFailed(exception: Exception) {
                Log.w("Error", "Error al obtener data user loguiado", exception)
            }
        })
    }

    private fun getSeries(it: ArrayList<String>) {
        for (post in it) {
            db.getDataSerie(post, object : Callback<Serie> {
                override fun onSuccess(result: Serie?) {
                    result?.let {
                        addListHelp(it)
                    }
                }

                override fun onFailed(exception: Exception) {
                    Log.d("Error", "Error al obtener serie fav", exception)
                }
            })
        }
    }

    private fun addListHelp(serie: Serie) {
        this.listHelp.add(serie)
        val order = listHelp.sortedWith(compareBy<Serie> { it.day }.thenBy { it.hour }).reversed()
        this.listFavorites.value = order
    }

    fun getLiveDataSeriesFav():LiveData<List<Serie>> {
        return this.listFavorites
    }

    fun setListSeriesFav(data : ArrayList<Serie>){
        this.listFavorites.value = data
    }
}