package com.example.appseries.adapter

import com.example.appseries.model.Serie

interface HomeListener {
    fun onSerieClicked(serie: Serie, position: Int)
}