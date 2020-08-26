package com.example.appseries.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appseries.adapter.RealtimeDataListener
import com.example.appseries.model.Comment
import com.example.appseries.model.User
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices
import java.lang.Exception

class CommentsViewModel : ViewModel() {
    private val db = SeriesServices()
    private val listComments = MutableLiveData<List<Comment>>()
    private var post_id: String = ""

    fun getComments() {
        db.getComment(post_id, object : Callback<List<Comment>> {
            override fun onSuccess(result: List<Comment>?) {
                if (result != null) {
                    setListComments(result)
                }
            }

            override fun onFailed(exception: Exception) {
                Log.w("Error commentsVM", "Error al obtener los comentarios del post", exception.cause)
            }
        })
    }

    fun setPostId(post_id: String) {
        this.post_id = post_id
    }

    fun setListComments(data: List<Comment>) {
        this.listComments.value = data
    }

    fun getLiveDataComments(): LiveData<List<Comment>> {
        return this.listComments
    }

    fun suscribeToChanges() {
        db.listenForUpdateComments(post_id, object : RealtimeDataListener<List<Comment>> {
            override fun onDataChange(updatedData: List<Comment>) {
                setListComments(updatedData)
            }

            override fun onError(exception: Exception) {
                Log.w("Error commentsVM", "Error realtime", exception.cause)
            }
        })
    }
}