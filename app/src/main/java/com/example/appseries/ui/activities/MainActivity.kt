package com.example.appseries.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.appseries.R
import com.example.appseries.data.UserSingleton
import com.example.appseries.model.User
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val db = SeriesServices()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadSingleton()
        configNav()
    }

    private fun configNav() {
        NavigationUI.setupWithNavController(
            nav_menu,
            Navigation.findNavController(this, R.id.mainFragment)
        )
    }

    private fun loadSingleton() {
        db.getDataUser(object : Callback<User> {
            override fun onSuccess(result: User?) {
                result?.let { user ->
                    UserSingleton.getInstance().userId = user.userId
                    UserSingleton.getInstance().nombre = user.nombre
                    UserSingleton.getInstance().photosUploaded = user.photosUploaded
                    UserSingleton.getInstance().follow = user.follow
                    UserSingleton.getInstance().followers = user.followers
                }
                Log.d("Singleton", "singleton : ${UserSingleton.getInstance().nombre}")
            }

            override fun onFailed(exception: Exception) {
                Log.w("SeriesServices", "Error al cargar singleton", exception)
            }
        })
    }
}