package com.example.appseries.adapter

interface UploadListener<T> {
    fun onUpload(imageUrl: T)
}