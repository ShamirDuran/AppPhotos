package com.example.appseries.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appseries.R
import com.example.appseries.model.User
import com.squareup.picasso.Picasso

class SearchUserAdapter(val listener: SearchListener) :
    RecyclerView.Adapter<SearchUserAdapter.ViewHolder>() {

    private var listUsers = ArrayList<User>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchUserAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val searchView = inflater.inflate(R.layout.item_user, parent, false)
        return ViewHolder(searchView)
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }

    override fun onBindViewHolder(holder: SearchUserAdapter.ViewHolder, position: Int) {
        val user = listUsers[position]
        holder.username.text = user.nombre

        if (user.photo != "") {
            Picasso.get().load(user.photo).into(holder.photoUser)
        } else {
            holder.photoUser.setImageResource(R.drawable.photo_perfil_clean)
        }

        holder.itemView.setOnClickListener {
            listener.onFriendClicked(user, position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username = itemView.findViewById<TextView>(R.id.tvUsername)
        val photoUser = itemView.findViewById<ImageView>(R.id.ivPhotoUser)
    }

    fun updateListUser(data: List<User>?) {
        if (data != null) {
            listUsers.clear()
            listUsers.addAll(data)
            notifyDataSetChanged()
        }
    }
}