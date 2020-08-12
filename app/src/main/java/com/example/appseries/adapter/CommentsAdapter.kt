package com.example.appseries.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.appseries.R
import com.example.appseries.model.Comment
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    private var listComments = ArrayList<Comment>()

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
        val comment:Comment = listComments[position]
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var username: TextView = itemView.findViewById(R.id.tvUsernameComment)
        var photoUserComment: ImageView = itemView.findViewById(R.id.ivPhotoUserComment)
        var contentComment: TextView = itemView.findViewById(R.id.tvContentComment)
    }
}