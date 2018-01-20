package com.pedromassango.herenow.ui.main.fragments.places

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.row_place.view.*

/**
 * Created by Pedro Massango on 1/17/18.
 */
class PlaceVH(val view: View): RecyclerView.ViewHolder(view) {

    fun bindViews(place: Place){
        with(view){
            img_place_icon.setImageResource( place.placeIcon)
            tv_place_type.text = place.placeName
        }
    }
}