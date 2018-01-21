package com.pedromassango.herenow.ui.main.fragments.places

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pedromassango.herenow.R
import com.pedromassango.herenow.data.model.Place
import com.pedromassango.herenow.extras.ActivityUtils
import com.pedromassango.herenow.ui.main.MainActivity

/**
 * Created by Pedro Massango on 1/16/18.
 *
 * List places.
 */
class FragmentBottomSheetPlaces : Fragment(), IPlaceClickListener {

    companion object {
        private var INSTANCE: FragmentBottomSheetPlaces? = null
        fun getInstance(): FragmentBottomSheetPlaces {
            if (INSTANCE == null) {
                INSTANCE = FragmentBottomSheetPlaces()
            }
            return INSTANCE!!
        }
    }

    @SuppressLint("Recycle")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = RecyclerView(context)
        root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)

        val layoutManager = GridLayoutManager(context, 2)
        root.layoutManager = layoutManager
        root.setHasFixedSize(true)

        val places = arrayListOf<Place>()

        val placesNames = resources.getStringArray(R.array.places_names)
        val placesTypes = resources.getStringArray(R.array.places_type)
        val placesIcons = resources.obtainTypedArray(R.array.places_icons)

        // Add places to list
        placesNames.mapIndexedTo(places) { index, i ->
            Place(i, placesIcons.getResourceId(index, 0).toString(), placesTypes[index])
        }

        // Setting up adapter and pass it to recyclerView
        val placesAdapter = PlacesAdapter(places, this)
        root.adapter = placesAdapter
        root.setHasFixedSize(true)

        return root
    }

    /**
     * Invoked when a place on list is clicked.
     */
    override fun invoke(place: Place) {

        // Change activity title
        activity!!.title = place.placeName

        // Change mainActivity is currentFragmentId
        (activity as MainActivity).currentFragmentId = 1024
        //Collapse BottomSheet after Place click
        (activity as MainActivity).bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED

        // On Place click, show a fragment to list all neaby places
        ActivityUtils.replaceFragment(fragmentManager!!,
                fragment = FragmentShowPlacesOnMap.getInstance(place.placeType))
    }
}