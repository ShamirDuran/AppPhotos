package com.example.appseries.viewmodel

import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appseries.adapter.PostListener
import com.example.appseries.adapter.RealtimeDataListener
import com.example.appseries.model.Serie
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices
import java.lang.Exception

class FriendViewModel : ViewModel() {
    private val db = SeriesServices()
    private val listPost = MutableLiveData<List<Serie>>()

    fun getDataPosts(userID: String) {
        db.getSeries(object : Callback<List<Serie>> {
            override fun onSuccess(result: List<Serie>?) {
                setDataPosts(result)
            }

            override fun onFailed(exception: Exception) {
                Log.w("SeriesServices", "Error al traer series de amigo", exception)
            }

        }, userID)
    }

    fun getLiveDataPost(): LiveData<List<Serie>> {
        return this.listPost
    }

    private fun setDataPosts(result: List<Serie>?) {
        this.listPost.value = result
    }

    fun suscribeToChanges(userID: String) {
        db.listenForUpdates(object : RealtimeDataListener<List<Serie>> {
            override fun onDataChange(updatedData: List<Serie>) {
                setDataPosts(updatedData)
            }

            override fun onError(exception: Exception) {
                Log.w("SeriesServices", "Error listener de postFriend", exception)
            }
        }, userID)
    }
}