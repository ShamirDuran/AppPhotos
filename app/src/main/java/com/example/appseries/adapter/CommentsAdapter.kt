package com.example.appseries.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.appseries.R
import com.example.appseries.model.Comment
import com.example.appseries.model.User
import com.example.appseries.network.Callback
import com.example.appseries.network.SeriesServices
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_comment.view.*
import java.lang.Exception

class CommentsAdapter(val commentListener: CommentListener) :
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    private var listComments = ArrayList<Comment>()
    private var listUser = ArrayList<User>()
    private var db = SeriesServices()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val commentView = inflater.inflate(R.layout.item_comment, parent, false)
        return ViewHolder(commentView)
    }

    override fun getItemCount(): Int {
        return listComments.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment: Comment = listComments[position]

        try {
            val user: User = listUser[position]
            holder.username.text = user.nombre
            if (user.photo!="") Picasso.get().load(user.photo).into(holder.photoUserComment)
        } catch (e: Exception) {
            Log.e("Error commentAdapter", "Error tamaño de listaUser", e)
        }

        holder.contentComment.text = comment.content

        holder.itemView.setOnClickListener {
            commentListener.onCommentClicked(comment, position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var username: TextView = itemView.findViewById(R.id.tvUsernameComment)
        var contentComment: TextView = itemView.findViewById(R.id.tvContentComment)
        var photoUserComment: ImageView = itemView.findViewById(R.id.ivPhotoUserComment)
    }

    fun updateListComments(data: List<Comment>?) {
        if (data != null) {
            listComments.clear()
            listComments.addAll(data)
            listUser.clear()
            getDataUsers(data)
        }
    }

    private fun getDataUsers(data: List<Comment>) {
        for (post in data) {
            db.getDataUser(object : Callback<User> {
                override fun onSuccess(result: User?) {
                    if (result != null) {
                        setListUser(result)
                    }
                }

                override fun onFailed(exception: Exception) {
                    Log.e("Error commentAdapter", "Error obteniendo data de user", exception)
                }

            }, post.user_id)
        }
    }

    private fun setListUser(result: User) {
        this.listUser.add(result)
        if (listComments.size == listUser.size) {
            Log.d("Mensaje", "Tamaño listUser ${listUser.size}")
            Log.d("Mensaje", "Tamaño listComments ${listComments.size}")
            notifyDataSetChanged()
        }
    }
}