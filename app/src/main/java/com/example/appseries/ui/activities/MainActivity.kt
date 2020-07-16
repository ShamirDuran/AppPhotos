package com.example.appseries.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.appseries.R
import com.example.appseries.ui.fragments.FavoritesFragment
import com.example.appseries.ui.fragments.HomeFragment
import com.example.appseries.ui.fragments.PerfilFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configNav()


    }

    private fun configNav() {
        NavigationUI.setupWithNavController(
            nav_menu,
            Navigation.findNavController(this, R.id.mainFragment)
        )
    }
}