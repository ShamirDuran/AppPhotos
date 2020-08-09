package com.example.appseries.adapter

import com.example.appseries.model.Serie

interface PostListener {
    fun onPostClicked(serie: Serie, position: Int)
}