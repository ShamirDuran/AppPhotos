package com.example.appseries.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appseries.adapter.RealtimeDataListener
import com.example.appseries.model.Serie
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices
import java.lang.Exception

const val TAG = "SerieViewModel"

class SeriesViewModel : ViewModel() {

    private val db = SeriesServices()
    private val listSeries = MutableLiveData<List<Serie>>()
    private val listSeriesFav = MutableLiveData<List<Serie>>()

    init {
        getSeries()
        getSeriesFav()
    }

    private fun getSeries() {
        db.getSeries(object : Callback<List<Serie>> {
            override fun onSuccess(result: List<Serie>?) {
                if (result != null) {
                    setListSerie(result)
                }
            }

            override fun onFailed(exception: Boolean) {
                Log.w("SerieViewModel: ", "error al usar SerieService", exception)
            }
        })
    }

    private fun getSeriesFav() {
        db.getSeriesFav(object : Callback<List<Serie>> {
            override fun onSuccess(result: List<Serie>?) {
                if (result != null) {
                    setListSerieFav(result)
                }
            }

            override fun onFailed(exception: Boolean) {
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

    private fun setListSerie(lista: List<Serie>) {
        this.listSeries.value = lista
    }

    private fun setListSerieFav(lista: List<Serie>) {
        this.listSeriesFav.value = lista
    }

    fun suscribeToChanges() {
        db.listenForUpdates(object : RealtimeDataListener<List<Serie>> {
            override fun onDataChange(updatedData: List<Serie>) {
                setListSerie(updatedData)
                getSeriesFav()
            }

            override fun onError(exception: Exception) {
                Log.w(TAG, "Error en snapshot y set", exception)
            }
        })
    }
}