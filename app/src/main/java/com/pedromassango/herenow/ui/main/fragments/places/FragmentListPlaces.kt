package com.pedromassango.herenow.ui.main.fragments.places

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pedromassango.herenow.R
import com.pedromassango.herenow.extras.ActivityUtils
import com.pedromassango.herenow.ui.main.MainActivity

/**
 * Created by Pedro Massango on 1/16/18.
 *
 * List places.
 */
class FragmentListPlaces : Fragment(), IPlaceClickListener {

    companion object {
        var INSTANCE: FragmentListPlaces? = null
        fun getInstance(): FragmentListPlaces {
            if (INSTANCE == null) {
                INSTANCE = FragmentListPlaces()
            }
            return INSTANCE!!
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

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        root.layoutManager = layoutManager
        root.setHasFixedSize(true)

        val places = arrayListOf<Place>()

        val placesNames = resources.getStringArray(R.array.places_names)
        val placesIcons = resources.obtainTypedArray(R.array.places_icons)

        // Add places to list
        placesNames.mapIndexedTo(places) { index, i -> Place(i, placesIcons.getResourceId(index, 0)) }

        // Setting up adapter and pass it to recyclerView
        val placesAdapter = PlacesAdapter(places, this)
        root.adapter = placesAdapter

        return root
    }

    /**
     * Invoked when a place on list is clicked.
     */
    override fun invoke(place: Place) {

        // Change activity title
        activity.title = place.placeName

        // Change mainActivity is currentFragmentId
        (activity as MainActivity).currentFragmentId = 1024

        // On Place click, show a fragment to list all neaby places
        ActivityUtils.replaceFragment(fragmentManager, fragment = FragmentShowPlacesOnMap.getInstance(place.placeId))
    }

}