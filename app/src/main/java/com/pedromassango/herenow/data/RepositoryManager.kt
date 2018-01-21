package com.pedromassango.herenow.data

import com.pedromassango.herenow.data.local.ContactsLocalRepository
import com.pedromassango.herenow.data.local.NearbyPlacesLocalRepository
import com.pedromassango.herenow.data.preferences.PreferencesHelper
import com.pedromassango.herenow.data.remote.ContactsRemoteRepository

/**
 * Created by pedromassango on 12/30/17.
 */
object RepositoryManager {

    fun contactsRepository(helper: PreferencesHelper) =
            ContactsRepository.getInstance(
                    ContactsRemoteRepository.getInstance(helper),
                    ContactsLocalRepository.getInstance())

    fun nearbyPlacesRepository() =
            NearbyPlacesRepository(
                    NearbyPlacesLocalRepository.getInstance())

}