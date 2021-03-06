package com.pedromassango.herenow.ui.main.fragments.map

import com.google.android.gms.maps.model.LatLng
import com.pedromassango.herenow.data.model.Contact

/**
 * Created by pedromassango on 12/29/17.
 */
class MapContract {

    interface View{

        fun showFriendOnMap(contact: Contact)
        fun showGetFriendsLocationError()
        fun removeLoader()
        fun showNoFriendsMessage(showDialog: Boolean)
        fun showLoader()
        fun getDistance(userLatLng: LatLng, friendLatLng: LatLng): Float
    }

    interface Presenter{
        fun onUserLocationChanged(latitude: Double, longitude: Double)
        fun showFriendsOnMap()
    }
}