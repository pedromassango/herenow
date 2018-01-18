package com.pedromassango.herenow.ui.main.fragments.places

import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.pedromassango.herenow.ui.main.fragments.BaseMapFragment

/**
 * Created by Pedro Massango on 1/18/18.
 */
class FragmentShowPlacesOnMap : BaseMapFragment() {

    companion object {
        val PLACES_TYPE = "place_id"

        fun getInstance(placeType: Int): FragmentShowPlacesOnMap {
            val bundle = Bundle()
            bundle.putInt(PLACES_TYPE, placeType)

            val instance = FragmentShowPlacesOnMap()
            instance.arguments = bundle
            return instance
        }

    }

    // Received places type to show
    var placesType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        placesType = arguments.getInt(PLACES_TYPE)
    }

    override fun onMapReady(mMap: GoogleMap?) {
        super.onMapReady(mMap)

        //TODO: request places
    }
}