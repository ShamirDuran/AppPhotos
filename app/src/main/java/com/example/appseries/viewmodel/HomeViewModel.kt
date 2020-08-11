package com.example.appseries.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appseries.model.Serie
import com.example.appseries.model.User
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel : ViewModel() {
    private val listSeriesHome = MutableLiveData<List<Serie>>()

    init {

    }

    private fun setListSeriesHome(data: List<Serie>) {
        this.listSeriesHome.value = data
    }

    fun getLiveDataSeriesHome(): LiveData<List<Serie>> {
        return this.listSeriesHome
    }


}