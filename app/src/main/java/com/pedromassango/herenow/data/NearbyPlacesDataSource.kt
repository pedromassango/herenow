package com.pedromassango.herenow.data

import com.google.android.gms.maps.model.LatLng
import com.pedromassango.herenow.data.model.Place

/**
 * Created by Pedro Massango on 1/21/18.
 */
interface NearbyPlacesDataSource {

    interface IRequestNearbyPlacesListener{
        fun onSuccess(result: MutableList<Place>)
        fun onError()
    }

    fun getNearbyPlaces(userLocation: LatLng, placeType: String,
                        iRequestNearbyPlacesListener: IRequestNearbyPlacesListener?)

}