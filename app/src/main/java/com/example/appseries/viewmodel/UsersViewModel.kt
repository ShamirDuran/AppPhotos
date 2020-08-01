package com.example.appseries.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appseries.adapter.RealtimeDataListener
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices
import com.example.appseries.model.User
import java.lang.Exception

class UsersViewModel : ViewModel() {
    private val db = SeriesServices()
    private var user = MutableLiveData<User>()
    private val TAG = "UserViewModel"

    init {
        getDataUser()
    }

    private fun getDataUser() {
        db.getDataUser(object : Callback<User> {
            override fun onSuccess(result: User?) {
                setDataUser(result)
            }

            override fun onFailed(exception: Exception) {
                Log.w(TAG, "Error: ", exception)
            }
        })
    }

    fun getLiveDataUser() : LiveData<User> {
        return user
    }


    private fun setDataUser(user: User?) {
        this.user.value = user
    }

    fun suscribeToChanges(){
        db.listenForUpdatesUser(object : RealtimeDataListener<User> {
            override fun onDataChange(updatedData: User) {
                setDataUser(updatedData)
            }

            override fun onError(exception: Exception) {
                Log.w(TAG, "Error snapshot: ", exception)
            }
        })
    }
}