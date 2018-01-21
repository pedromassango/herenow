package com.pedromassango.herenow.data.local

import com.google.android.gms.maps.model.LatLng
import com.pedromassango.herenow.data.NearbyPlacesDataSource
import com.pedromassango.herenow.data.model.Place

/**
 * Created by Pedro Massango on 1/21/18.
 */
class NearbyPlacesLocalRepository : NearbyPlacesDataSource {

    companion object {
        private var INSTANCE: NearbyPlacesLocalRepository? = null

        fun getInstance(): NearbyPlacesLocalRepository {
            if (INSTANCE == null) {
                INSTANCE = NearbyPlacesLocalRepository()
            }
            return INSTANCE!!
        }
    }

    override fun getNearbyPlaces(userLocation: LatLng, placeType: String,
                                 iRequestNearbyPlacesListener: NearbyPlacesDataSource.IRequestNearbyPlacesListener?) {

    }

    fun savePlaces(places: ArrayList<Place>) {

    }
}