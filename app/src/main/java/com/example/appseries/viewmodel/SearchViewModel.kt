package com.example.appseries.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appseries.data.UserSingleton
import com.example.appseries.model.User
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices
import java.lang.Exception

class SearchViewModel : ViewModel() {
    private val db = SeriesServices()
    private val listUsers = MutableLiveData<List<User>>()

    fun getDataSearch(ref: String) {
        db.getSearchUser(ref, object : Callback<List<User>> {

            override fun onSuccess(result: List<User>?) {
                result?.let {
                    val users = ArrayList<User>()
                    it.forEach { user ->
                        if (user.nombre.contains(ref) && user.userId != UserSingleton.getInstance().userId){
                            users.add(user)
                        }
                    }
                    setListUsers(users)
                }
            }

            override fun onFailed(exception: Boolean) {
                Log.w("SeriesServices", "Error getSearchUser", exception)
            }
        })
    }

    fun setListUsers(data: List<User>) {
        listUsers.value = null
        listUsers.value = data
    }

    fun getLiveListUsers(): LiveData<List<User>> {
        return listUsers
    }
}