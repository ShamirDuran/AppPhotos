package com.example.appseries.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appseries.R
import com.example.appseries.model.Serie
import com.squareup.picasso.Picasso
import java.util.zip.Inflater

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    private var listSeriesFav = ArrayList<Serie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val favView = inflater.inflate(R.layout.item_favorites, parent, false)
        return ViewHolder(favView)
    }

    override fun getItemCount(): Int = listSeriesFav.size

    override fun onBindViewHolder(holder: FavoritesAdapter.ViewHolder, position: Int) {
        val serieFav = listSeriesFav[position]
        holder.nombreSerieFav.text = serieFav.nombre
        holder.descSerieFav.text = serieFav.desc

        if (serieFav.imageUrl != ""){
            Picasso.get().load(serieFav.imageUrl).into(holder.imagenSerieFav)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreSerieFav = itemView.findViewById<TextView>(R.id.tvTitleItemFav)
        val descSerieFav = itemView.findViewById<TextView>(R.id.tvDescItemFav)
        val imagenSerieFav = itemView.findViewById<ImageView>(R.id.ivItemFav)
    }

    fun updateSeriesFav(data: List<Serie>?) {
        if (data != null) {
            listSeriesFav.clear()
            listSeriesFav.addAll(data)
            notifyDataSetChanged()
        }
    }
}