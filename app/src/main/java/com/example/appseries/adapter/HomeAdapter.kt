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


class HomeAdapter(val homeListener: HomeListener) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private val listSeries = ArrayList<Serie>()

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
//
//        Glide.with(holder.itemView.context)
//            .load(serie.imageUrl)
//            .into(holder.imagen)

        if (serie.imageUrl != ""){
            Picasso.get().load(serie.imageUrl).into(holder.imagen)
        }

        holder.nombre.text = serie.nombre

        holder.itemView.setOnClickListener{
            homeListener.onSerieClicked(serie, position)
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imagen: ImageView = itemView.findViewById<ImageView>(R.id.ivItemHome)
        var nombre: TextView = itemView.findViewById<TextView>(R.id.tvItemHome)
    }

    fun updateListSeries(data: List<Serie>) {
        listSeries.clear()
        listSeries.addAll(data)
        notifyDataSetChanged()
    }
}