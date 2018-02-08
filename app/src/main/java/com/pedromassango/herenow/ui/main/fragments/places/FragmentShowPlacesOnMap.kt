package com.pedromassango.herenow.ui.main.fragments.places

import android.location.Location
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pedromassango.herenow.R
import com.pedromassango.herenow.app.HereNow
import com.pedromassango.herenow.data.NearbyPlacesDataSource
import com.pedromassango.herenow.data.RepositoryManager
import com.pedromassango.herenow.data.model.Place
import com.pedromassango.herenow.ui.main.fragments.BaseMapFragment

/**
 * Created by Pedro Massango on 1/18/18.
 *
 * Show places on map
 */
class FragmentShowPlacesOnMap : BaseMapFragment(), NearbyPlacesDataSource.IRequestNearbyPlacesListener {

    companion object {
        const val PLACES_TYPE = "place_type"

        fun getInstance(placeType: String): FragmentShowPlacesOnMap {
            val bundle = Bundle()
            bundle.putString(PLACES_TYPE, placeType)

            val instance = FragmentShowPlacesOnMap()
            instance.arguments = bundle
            return instance
        }

    }

    // Received places type to show
    private var placesType = ""
    // save fisrt time places set to map
    private var placesOnMap: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        placesType = arguments!!.getString(PLACES_TYPE)

        // here whe change the period of location update
        timeUpdate = 150000
    }

    override fun onLocationChanged(location: Location?) {

        // Show a loader
        //super.dismissLoader()
        //super.loader()

        // Request places
        RepositoryManager.nearbyPlacesRepository()
                .getNearbyPlaces(LatLng(location!!.latitude, location.longitude),
                        placesType, this)
    }

    override fun onSuccess(result: MutableList<Place>) {
        HereNow.logcat("show placess - onSuccess")
        dismissLoader()

        result.forEach { place ->

            val placeLocationmarker = MarkerOptions()
            // set the marker at first time
            placeLocationmarker.title(place.placeName).snippet(place.vicinity)
            placeLocationmarker.position(LatLng(place.lat, place.lng))
            placeLocationmarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_blue_grey_900_24dp))
            map!!.addMarker(placeLocationmarker)

            if (!placesOnMap) {
                map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(place.lat, place.lng), 15F))
                placesOnMap = true
            }
        }
    }

    override fun onError() {
        dismissLoader()
        Toast.makeText(activity, "Falha ao obter lugares proximo de você.", Toast.LENGTH_SHORT).show()
        Toast.makeText(activity, "Tente novamente!", Toast.LENGTH_LONG).show()
    }
}