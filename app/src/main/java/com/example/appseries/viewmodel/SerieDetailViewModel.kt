package com.example.appseries.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appseries.adapter.RealtimeDataListener
import com.example.appseries.model.Serie
import com.example.appseries.network.SeriesServices
import java.lang.Exception

class SerieDetailViewModel : ViewModel() {
    private val db = SeriesServices()
    private var serie = MutableLiveData<Serie>()
    private val TAG = "SerieDetailDialog"

    fun getLivedataSerie(): LiveData<Serie> {
        return serie
    }

    fun setDataSerie(serie: Serie) {
        this.serie.value = serie
    }

    fun suscribeToChanges() {
        Log.d(TAG, "Error: ${serie.value}")
        db.listenForUpdatesSerieDetail(serie.value!!, object : RealtimeDataListener<Serie> {
            override fun onDataChange(updatedData: Serie) {
                setDataSerie(updatedData)
            }

            override fun onError(exception: Exception) {
                Log.w(TAG, "Error SerieDetailViewModel: ", exception)
            }
        })
    }
}