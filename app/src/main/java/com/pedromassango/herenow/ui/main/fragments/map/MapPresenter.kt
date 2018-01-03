package com.pedromassango.herenow.ui.main.fragments.map

import com.pedromassango.herenow.app.HereNow.Companion.logcat
import com.pedromassango.herenow.data.ContactsDataSource
import com.pedromassango.herenow.data.ContactsRepository
import com.pedromassango.herenow.data.model.Contact

/**
 * Created by pedromassango on 12/29/17.
 */
class MapPresenter(private val view: MapContract.View,
                   private val contactsRepository: ContactsRepository) : MapContract.Presenter, ContactsDataSource.ILocationListener {

    //TODO: get friends location to show on MAP

    override fun onUserLocationChanged(latitude: Double, longitude: Double) {
        logcat("onUserLocationChanged: $latitude && $longitude")

        contactsRepository.updateUserLocation(latitude, longitude)
    }

    // Synchronize the friends location (if have) to show on map
    override fun showFriendsOnMap() {
        view.showLoader()

        contactsRepository.keepFriendsLocationSync(this)
    }

    override fun onAllowed(contact: Contact) {
        view.removeLoader()
        view.showFriendOnMap(contact)
    }

    override fun onNoFriends() {
        view.removeLoader()
        view.showNoFriendsMessage()
    }

    override fun onError() {
        view.removeLoader()
        view.showGetFriendsLocationError()
    }
}