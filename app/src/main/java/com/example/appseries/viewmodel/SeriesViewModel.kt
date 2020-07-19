package com.example.appseries.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appseries.model.Serie
import com.example.appseries.network.SeriesServices

class SeriesViewModel : ViewModel() {

    private val seriesService = SeriesServices()
    val listSeries = MutableLiveData<List<Serie>>()
    private val listSeriesFav: MutableLiveData<List<Serie>> = MutableLiveData<List<Serie>>()

    init {
        getSeries()
    }

    fun setListSeries(listSeries: List<Serie>) {
        this.listSeries.value = listSeries
    }

    fun setListSeriesFav(listSeriesFav: List<Serie>) {
        this.listSeriesFav.value = listSeriesFav
    }

    private fun getSeries() {
        setListSeries(seriesService.getSeriesService())
        setListSeriesFav(seriesService.getSeriesFavService())
    }

    fun getLiveDataListSeries(): LiveData<List<Serie>> {
        return listSeries
    }

    fun getLiveDataSeriesFav(): LiveData<List<Serie>> {
        return listSeriesFav
    }
}