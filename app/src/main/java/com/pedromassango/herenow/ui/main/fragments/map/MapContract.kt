package com.pedromassango.herenow.ui.main.fragments.map

import com.pedromassango.herenow.data.model.Contact
import com.pedromassango.herenow.ui.main.IPermissionListener

/**
 * Created by pedromassango on 12/29/17.
 */
class MapContract {

    interface View{

        fun requestLocationPermission(iPermissionListener: IPermissionListener)
        fun showFriendOnMap(contact: Contact)
        fun showGetFriendsLocationError()
        fun removeLoader()
        fun showNoFriendsMessage(showDialog: Boolean)
        fun showLoader()
    }

    interface Presenter{
        fun onUserLocationChanged(latitude: Double, longitude: Double)
        fun showFriendsOnMap()
    }
}