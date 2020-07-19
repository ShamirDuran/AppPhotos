package com.example.appseries.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appseries.model.Serie
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices
import java.lang.Exception

class SeriesViewModel : ViewModel() {

    private val db = SeriesServices()
    private val listSeries = MutableLiveData<List<Serie>>()
    private val listSeriesFav: MutableLiveData<List<Serie>> = MutableLiveData<List<Serie>>()

    init {
        getSeries()
    }

    private fun getSeries() {
        db.getSeries(object : Callback<List<Serie>> {
            override fun onSuccess(result: List<Serie>?) {
                listSeries.value = result
            }

            override fun onFailed(exception: Exception) {
                Log.w("SerieViewModel: ", "error al usar SerieService", exception)
            }

        })

        db.getSeriesFav(object : Callback<List<Serie>> {
            override fun onSuccess(result: List<Serie>?) {
                listSeriesFav.value = result
            }

            override fun onFailed(exception: Exception) {
                Log.w("SerieViewModel: ", "error al usar SerieServiceFav", exception)
            }
        })
    }

    fun getLiveDataListSeries(): LiveData<List<Serie>> {
        return listSeries
    }

    fun getLiveDataSeriesFav(): LiveData<List<Serie>> {
        return listSeriesFav
    }
}