package com.pedromassango.herenow.data

import com.google.android.gms.maps.model.LatLng
import com.pedromassango.herenow.app.HereNow
import com.pedromassango.herenow.data.model.Place
import com.pedromassango.herenow.data.remote.GetNearbyPlacesData

/**
 * Created by Pedro Massango on 1/21/18.
 */
class NearbyPlacesRepository : NearbyPlacesDataSource {

    override fun getNearbyPlaces(userLocation: LatLng, placeType: String,
                                 iRequestNearbyPlacesListener: NearbyPlacesDataSource.IRequestNearbyPlacesListener?) {

        GetNearbyPlacesData(userLocation, placeType, object : NearbyPlacesDataSource.IRequestNearbyPlacesListener {
            override fun onSuccess(result: MutableList<Place>) {

                HereNow.logcat("NearbyPlacesRepository - getNearbyPlaces - success")
                iRequestNearbyPlacesListener!!.onSuccess(result)
            }

            override fun onError() = iRequestNearbyPlacesListener!!.onError()

        }).execute() // start task to get places

    }
}