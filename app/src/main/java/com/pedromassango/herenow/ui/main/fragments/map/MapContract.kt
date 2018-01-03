package com.pedromassango.herenow.ui.main.fragments.map

import com.pedromassango.herenow.data.model.Contact

/**
 * Created by pedromassango on 12/29/17.
 */
class MapContract {

    interface View{
        fun showFriendOnMap(contact: Contact)
        fun showGetFriendsLocationError()
        fun removeLoader()
        fun showNoFriendsMessage()
        fun showLoader()

    }

    interface Presenter{
        fun onUserLocationChanged(latitude: Double, longitude: Double)
        fun showFriendsOnMap()

    }
}