package com.example.appseries.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appseries.model.Serie
import com.example.appseries.model.User
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices

class HomeViewModel : ViewModel() {
    private val db = SeriesServices()
    private val listHelp = ArrayList<Serie>()
    private val listHelpUser = ArrayList<User>()
    private val listSeriesHome = MutableLiveData<List<Serie>>()
    private val listUsers = MutableLiveData<List<User>>()

    init {
        getSeriesFollow()
    }

    private fun getSeriesFollow() {
        db.getDataUser(object : Callback<User> {
            override fun onSuccess(result: User?) {
                result?.let { user ->
                    user.follow?.let {
                        getPhotos(it)
                    }
                }
            }

            override fun onFailed(exception: Exception) {
                Log.w("Error HomeVM", "No se consiguio info user logueado", exception)
            }
        })
    }

    private fun setListSeriesHome(data: ArrayList<Serie>) {
        this.listSeriesHome.value = data
    }

    fun getLiveDataSeriesHome(): LiveData<List<Serie>> {
        return this.listSeriesHome
    }

    fun getLiveDataUsers():LiveData<List<User>> {
        return this.listUsers
    }

    private fun getPhotos(it: ArrayList<String>) {
        for (user_follow in it) {
            db.getSeries(object : Callback<List<Serie>> {
                override fun onSuccess(result: List<Serie>?) {
                    if (result != null) {
                        addArrayHelp(result)
                    }
                }

                override fun onFailed(exception: Exception) {
                    Log.w(
                        "Error HomeVM",
                        "No se obtuvo la lsita de seguidores para user logueado",
                        exception
                    )
                }
            }, user_follow)


            // Obtenemos la info del los que seguimos
            db.getDataUser(object:Callback<User>{
                override fun onSuccess(result: User?) {
                    if (result!=null) addArrayHelpUser(result)
                }

                override fun onFailed(exception: java.lang.Exception) {
                    Log.w("Error HomeVM", "No se pudo obtener la data del follower $user_follow")
                }

            }, user_follow)
        }
    }

    private fun addArrayHelpUser(result: User) {
        listHelpUser.add(result)
        listUsers.value = listHelpUser
    }

    private fun addArrayHelp(data: List<Serie>) {
        this.listHelp.addAll(data)
        val order = listHelp.sortedWith(compareBy<Serie> { it.day }.thenBy { it.hour }).reversed()
        this.listSeriesHome.value = order
    }
}