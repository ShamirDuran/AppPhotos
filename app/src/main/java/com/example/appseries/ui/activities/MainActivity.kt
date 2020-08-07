package com.example.appseries.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.appseries.R
import com.example.appseries.data.UserSingleton
import com.example.appseries.model.User
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices
import com.example.appseries.ui.fragments.FavoritesFragment
import com.example.appseries.ui.fragments.HomeFragment
import com.example.appseries.ui.fragments.PerfilFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val db = SeriesServices()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configNav()
        loadSingleton()
    }

    private fun loadSingleton() {
        db.getDataUser(object:Callback<User>{
            override fun onSuccess(result: User?) {
                result?.let { user ->
                    UserSingleton.getInstance().follow = user.follow
                    UserSingleton.getInstance().nombre = user.nombre
                    UserSingleton.getInstance().userId = user.userId
                }
                Log.d("Singleton",  "singleton : ${UserSingleton.getInstance().nombre}" )
            }

            override fun onFailed(exception: Exception) {
                Log.w("SeriesServices", "Error al cargar singleton", exception)
            }
        })
    }

    private fun configNav() {
        NavigationUI.setupWithNavController(
            nav_menu,
            Navigation.findNavController(this, R.id.mainFragment)
        )
    }
}