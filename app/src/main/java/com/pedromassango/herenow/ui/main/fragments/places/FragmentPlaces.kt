package com.pedromassango.herenow.ui.main.fragments.places

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pedromassango.herenow.R
import com.pedromassango.herenow.ui.main.fragments.BaseMapFragment

/**
 * Created by Pedro Massango on 1/16/18.
 *
 * List places.
 */
class FragmentPlaces : Fragment() {

    companion object {

        fun getInstance(): FragmentPlaces {
            return FragmentPlaces()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = RecyclerView(context)
        root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        root.layoutManager = layoutManager
        root.setHasFixedSize(true)

        val places = arrayListOf<Place>()

        val placesNames = resources.getStringArray(R.array.places_names)
        val placesIcons = resources.obtainTypedArray(R.array.places_icons)

        for ((index, i) in placesNames.withIndex()) {
            val place = Place(i, placesIcons.getResourceId(index, 0))
            places.add(place)
        }

        val placesAdapter = PlacesAdapter(places)
        root.adapter = placesAdapter

        return root
    }

    //TODO: complete places fragment
}