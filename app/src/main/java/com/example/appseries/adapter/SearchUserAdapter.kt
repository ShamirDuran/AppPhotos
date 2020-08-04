package com.example.appseries.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appseries.R
import com.example.appseries.model.User

class SearchUserAdapter : RecyclerView.Adapter<SearchUserAdapter.ViewHolder>() {

    private var listUsers = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserAdapter.ViewHolder {
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

//        if (user.imageUrl != "") {
//            Picasso.get().load(user.imageUrl).into(holder.photoUser)
//        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username = itemView.findViewById<TextView>(R.id.tvUsername)
//        val photoUser = itemView.findViewById<ImageView>(R.id.ivPhotoUser)
//        val userEmail = itemView.findViewById<TextView>(R.id.tvEmailUser)
    }

    fun updateListUser(data: List<User>?) {
        if (data != null) {
            listUsers.clear()
            listUsers.addAll(data)
            notifyDataSetChanged()
        }
    }
}