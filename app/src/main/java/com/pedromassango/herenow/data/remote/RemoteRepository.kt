package com.pedromassango.herenow.data.remote

import com.google.firebase.database.FirebaseDatabase
import com.pedromassango.herenow.data.ContactsDataSource
import com.pedromassango.herenow.data.PreferencesHelper
import com.pedromassango.herenow.data.model.Contact

/**
 * Created by pedromassango on 12/29/17.
 */
class RemoteRepository(val preferencesHelper: PreferencesHelper) : ContactsDataSource {

    val USERS_DATA = "users_data"
    val USERS_I_CAN_SEE = "users_i_can_see"
    val USERS_LOCATION = "users_location"

    val rootRef = FirebaseDatabase.getInstance().reference

    // USERS_DATA
    val usersDataRef = rootRef.child(USERS_DATA).ref
    val usersICanSeeRef = rootRef.child(USERS_I_CAN_SEE).ref

    override fun saveUserContacts(contact: Contact, iSaveListener: ContactsDataSource.ISaveListener) {
        val userPhone = preferencesHelper.phoneNumber

        usersICanSeeRef.child( userPhone).ref.setValue( contact.toMap())
                .addOnFailureListener { iSaveListener.onError() }
                .addOnSuccessListener { iSaveListener.onSaved() }
    }
}