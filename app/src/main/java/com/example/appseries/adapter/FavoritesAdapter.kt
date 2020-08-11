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

class FavoritesAdapter(val postListener: PostListener) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    private var listSeriesFav = ArrayList<Serie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val favView = inflater.inflate(R.layout.item_perfil, parent, false)
        return ViewHolder(favView)
    }

    override fun getItemCount(): Int = listSeriesFav.size

    override fun onBindViewHolder(holder: FavoritesAdapter.ViewHolder, position: Int) {
        val serieFav:Serie = listSeriesFav[position]

        if (serieFav.imageUrl != "") {
            Picasso.get().load(serieFav.imageUrl).into(holder.imagenSerieFav)
        }

        holder.imagenSerieFav.setOnClickListener {
            postListener.onPostClicked(serieFav, position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagenSerieFav: ImageView = itemView.findViewById(R.id.ivPhotoPost)
    }

    fun updateSeriesFav(data: List<Serie>?) {
        if (data != null) {
            listSeriesFav.clear()
            listSeriesFav.addAll(data)
            notifyDataSetChanged()
        }
    }
}