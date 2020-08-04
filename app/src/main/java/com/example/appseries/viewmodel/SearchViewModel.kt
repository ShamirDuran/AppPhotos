package com.example.appseries.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.appseries.model.User
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices
import java.lang.Exception

class SearchViewModel: ViewModel() {
    private val db = SeriesServices()
    private val listUsers = ArrayList<User>()

    fun getDataSearch(ref:String) {
        db.getSearchUser(ref, object : Callback<List<User>> {
            override fun onSuccess(result: List<User>?) {
                result?.let {
                    setListUsers(result)
                }
            }

            override fun onFailed(exception: Exception) {
                Log.w("SeriesServices", "Error getSearchUser", exception)
            }
        })
    }

    fun setListUsers(data : List<User>) {
        this.listUsers.clear()
        this.listUsers.addAll(data)
    }

    fun getListUsers(): List<User>{
        return listUsers
    }
}