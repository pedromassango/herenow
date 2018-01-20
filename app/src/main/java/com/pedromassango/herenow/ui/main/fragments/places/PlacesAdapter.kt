package com.pedromassango.herenow.ui.main.fragments.places

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pedromassango.herenow.R

/**
 * Created by Pedro Massango on 1/17/18.
 *
 * Adapt places item.
 */
class PlacesAdapter(val places: ArrayList<Place>,
                    val iPlaceClickListener: IPlaceClickListener) :
        RecyclerView.Adapter<PlaceVH>() {

    override fun onBindViewHolder(holder: PlaceVH?, position: Int) {
        val place = places[position]
        holder!!.bindViews(place)

        // Click listener for each place
        holder.itemView
                .setOnClickListener { iPlaceClickListener.invoke(place) }
    }

    override fun getItemCount(): Int = places.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlaceVH {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.row_place, parent, false)
        return PlaceVH(view)
    }
}