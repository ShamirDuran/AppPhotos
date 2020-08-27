package com.example.appseries.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appseries.R
import com.example.appseries.model.Serie
import com.example.appseries.model.User
import com.squareup.picasso.Picasso


class HomeAdapter(val postListener: PostListener) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private val listSeries = ArrayList<Serie>()
    private val listUser = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val homeView = inflater.inflate(R.layout.item_home, parent, false)
        return ViewHolder(homeView)
    }

    override fun getItemCount(): Int {
        return listSeries.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val serie = listSeries[position]

        listUser.forEach { user ->
            if (serie.userId == user.userId) {
                holder.username.text = user.nombre
                if (user.photo != "") Picasso.get().load(user.photo).into(holder.photoPerfil)
            }
        }
//
        if (serie.imageUrl != "") {
            Picasso.get().load(serie.imageUrl).into(holder.imagen)
        }

        holder.nombre.text = serie.nombre

        holder.itemView.setOnClickListener {
            postListener.onPostClicked(serie, position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imagen: ImageView = itemView.findViewById<ImageView>(R.id.ivItemHome)
        var nombre: TextView = itemView.findViewById<TextView>(R.id.tvItemHome)
        var username: TextView = itemView.findViewById<TextView>(R.id.tvUsername)
        var photoPerfil: ImageView = itemView.findViewById<ImageView>(R.id.ivPhotoPerfil)
    }

    fun updateListUser(data: List<User>) {
        this.listUser.clear()
        this.listUser.addAll(data)
        notifyDataSetChanged()

    }

    fun updateListSeries(data: List<Serie>) {
        this.listSeries.clear()
        this.listSeries.addAll(data)
        notifyDataSetChanged()
    }
}