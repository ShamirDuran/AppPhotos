package com.example.appseries.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.appseries.R
import com.example.appseries.model.Serie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_perfil.view.*

class FriendAdapter(val postListener: PostListener) : RecyclerView.Adapter<FriendAdapter.ViewHolder>() {
    private val listPost = ArrayList<Serie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val friendView = inflater.inflate(R.layout.item_perfil, parent, false)
        return ViewHolder(friendView)
    }

    override fun getItemCount(): Int {
        return listPost.size
    }

    override fun onBindViewHolder(holder: FriendAdapter.ViewHolder, position: Int) {
        val serie = listPost[position]
        if (serie.imageUrl != "") {
            Picasso.get().load(serie.imageUrl).into(holder.photo)
        }

        holder.photo.setOnClickListener {
            postListener.onPostClicked(serie, position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var photo: ImageView = itemView.findViewById(R.id.ivPhotoPost)
    }

    fun updateListPost(data: List<Serie>?) {
        if (data != null) {
            this.listPost.clear()
            this.listPost.addAll(data)
            notifyDataSetChanged()
        }
    }
}