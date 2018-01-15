package com.pedromassango.herenow.ui.main.fragments.map

import com.pedromassango.herenow.app.HereNow.Companion.logcat
import com.pedromassango.herenow.data.ContactsDataSource
import com.pedromassango.herenow.data.ContactsRepository
import com.pedromassango.herenow.data.model.Contact
import com.pedromassango.herenow.data.preferences.PreferencesHelper

/**
 * Created by pedromassango on 12/29/17.
 */
class MapPresenter(private val view: MapContract.View,
                   private val preferencesHelper: PreferencesHelper,
                   private val contactsRepository: ContactsRepository) : MapContract.Presenter, ContactsDataSource.ILocationListener {

    override fun onUserLocationChanged(latitude: Double, longitude: Double) {
        logcat("onUserLocationChanged: $latitude && $longitude")

        contactsRepository.updateUserLocation(latitude, longitude)
    }

    // Synchronize the friends location (if have) to show on map
    override fun showFriendsOnMap() {
        view.showLoader()

        contactsRepository.keepFriendsLocationSync(this)
    }

    override fun onAllowed(contact: ArrayList<Contact>) {
        logcat("MapPresenter: onAllowed -> ${contact.size}")
        view.removeLoader()

        contact.forEach {
            view.showFriendOnMap(it)
        }
    }

    override fun onNoFriends() {
        logcat("MapPresenter: onNoFriends.")
        view.removeLoader()

        val showDialog = preferencesHelper.noFriendsDialogShown

        view.showNoFriendsMessage(showDialog)

        if(showDialog){
            preferencesHelper.noFriendsDialogShown = true
        }

    }

    override fun onError() {
        view.removeLoader()
        view.showGetFriendsLocationError()
    }
}