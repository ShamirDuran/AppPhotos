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
import java.time.format.DateTimeFormatter

const val TAG = "SerieViewModel"

class SeriesViewModel : ViewModel() {

    private val db = SeriesServices()
    private val listSeries = MutableLiveData<List<Serie>>()

    init {
        getSeries()
    }

    private fun getSeries() {
        db.getSeries(object : Callback<List<Serie>> {
            override fun onSuccess(result: List<Serie>?) {
                if (result != null) {
                    setListSerie(result)
                }
            }

            override fun onFailed(exception: Exception) {
                Log.w("SerieViewModel: ", "error al usar SerieService", exception)
            }
        })
    }

    fun getLiveDataListSeries(): LiveData<List<Serie>> {
        return listSeries
    }

    private fun setListSerie(lista: List<Serie>) {
        this.listSeries.value = lista
    }

    fun suscribeToChanges() {
        db.listenForUpdates(object : RealtimeDataListener<List<Serie>> {
            override fun onDataChange(updatedData: List<Serie>) {
                setListSerie(updatedData)
            }

            override fun onError(exception: Exception) {
                Log.w(TAG, "Error en snapshot y set", exception)
            }
        })
    }
}