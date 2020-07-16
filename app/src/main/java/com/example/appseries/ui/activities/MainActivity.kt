package com.example.appseries.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.appseries.R
import com.example.appseries.ui.fragments.FavoritesFragment
import com.example.appseries.ui.fragments.HomeFragment
import com.example.appseries.ui.fragments.PerfilFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var homeFragment: HomeFragment
    private lateinit var favoritesFragment: FavoritesFragment
    private lateinit var perfilFragment: PerfilFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        homeFragment = HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragment, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()

        val bottomNavigation: BottomNavigationView = findViewById(R.id.nav_menu)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    homeFragment = HomeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.mainFragment, homeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()
                }
                R.id.favorites -> {
                    favoritesFragment = FavoritesFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.mainFragment, favoritesFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()
                }
                R.id.perfil -> {
                    perfilFragment = PerfilFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.mainFragment, perfilFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit()
                }
            }
            true
        }
    }
}