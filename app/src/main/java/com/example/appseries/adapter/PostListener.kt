package com.example.appseries.adapter

import com.example.appseries.model.Serie
import com.example.appseries.model.User

interface PostListener {
    fun onPostClicked(serie: Serie, position: Int)
}