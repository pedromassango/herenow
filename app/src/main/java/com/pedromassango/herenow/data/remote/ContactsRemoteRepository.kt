package com.pedromassango.herenow.data.remote

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pedromassango.herenow.app.HereNow.Companion.logcat
import com.pedromassango.herenow.data.ContactsDataSource
import com.pedromassango.herenow.data.model.Contact
import com.pedromassango.herenow.data.preferences.PreferencesHelper

/**
 * Created by pedromassango on 12/29/17.
 */
class ContactsRemoteRepository(private val preferencesHelper: PreferencesHelper) : ContactsDataSource {

    private val USERS_CONTACTS = "users_contacts"
    private val USERS_LOCATION = "users_location"

    companion object {
        private var INSTANCE: ContactsRemoteRepository? = null

        fun getInstance(preferencesHelper: PreferencesHelper): ContactsRemoteRepository {
            if (INSTANCE == null) {
                INSTANCE = ContactsRemoteRepository(preferencesHelper = preferencesHelper)
            }
            return INSTANCE!!
        }
    }

    // Current user phone number
    private val userPhone = preferencesHelper.phoneNumber

    private val rootRef = FirebaseDatabase.getInstance().reference

    // USERS_CONTACTS -> store the user friend list with information, to allow or deny see your location
    private val userContactsRef = rootRef.child(USERS_CONTACTS).child(userPhone).ref

    // USERS_LOCATION -> store the users location info
    private val usersLocationRef = rootRef.child(USERS_LOCATION).ref

    // Read from USERS_CONTACT && USERS_LOCATION
    override fun keepFriendsLocationSync(iLocationListener: ContactsDataSource.ILocationListener) {
        logcat("RemoteRepository: keepFriendsLocationSync...")

        getContacts(object : ContactsDataSource.IListener<Contact> {
            override fun onSuccess(data: ArrayList<Contact>) {

                if (data.isEmpty()) { // Check if the user have friends
                    iLocationListener.onNoFriends()
                    return
                }

                // For each friend, check if we can see their location
                data.forEach { friend ->

                    // Check if the user is on their friends allowed list
                    checkAuthorization(friend.phoneNumber, userPhone, object : ContactsDataSource.IContactListener {
                        override fun onSuccess(contact: Contact?) {
                            // check retrieved contact is not null
                            contact?.also {
                                // check if the user can se their location
                                if (it.allow) {
                                    logcat("friend allow you to see your their location")
                                    iLocationListener.onAllowed(friend)
                                }
                            } ?: Unit
                        }
                    })
                }
            }

            override fun onError() = iLocationListener.onError()
        })
    }

    /**
     * This method check if the user is on some friend list, and
     * retrieve then if was found.
     * @param friendNumber the friend number to check in
     * @param userNumber the current user number to check if is on list
     * @param iContactListener the listener to retrieve the contact.
     */
    private fun checkAuthorization(friendNumber: String, userNumber: String, iContactListener: ContactsDataSource.IContactListener) {
        rootRef.child(USERS_CONTACTS)
                .child(friendNumber)
                .child(userNumber)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) = iContactListener.onSuccess(null)

                    override fun onDataChange(p0: DataSnapshot?) {

                        if (p0 == null) {
                            logcat("checkAuthorization -> Not in list")
                            iContactListener.onSuccess(null)
                            return
                        }

                        if (!p0.exists()) {
                            logcat("checkAuthorization -> Not in list")
                            iContactListener.onSuccess(null)
                            return
                        }

                        // The user is on some friend list
                        logcat("checkAuthorization -> The user is on some friend list.")
                        logcat("checkAuthorization -> friend: $friendNumber user: $userNumber")

                        val userInFriend = p0.getValue(Contact::class.java)

                        // retrieve the contact
                        iContactListener.onSuccess(userInFriend)
                    }
                })
    }

    // Read from USERS_CONTACTS
    override fun getContacts(iListener: ContactsDataSource.IListener<Contact>?) {
        logcat("RemoteRepository: getContacts...")

        userContactsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) = iListener!!.onError()

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                if (!dataSnapshot!!.exists()) {
                    logcat("getContacts: onDataChange -> notExist")
                    iListener!!.onSuccess(arrayListOf())  // No user contacts
                    return
                }

                logcat("getContacts: onDataChange -> has data.")

                // Temp list to add fetched items
                val tempList = arrayListOf<Contact>()

                dataSnapshot.children.forEach {
                    val contact = it.getValue(Contact::class.java)
                    tempList.add(contact!!)
                }

                // Notify the listener
                iListener?.onSuccess(tempList)
            }
        })
        logcat("RemoteRepository: getContacts - DONE.")
    }

    // Save in USERS_CONTACTS
    override fun saveUserContacts(contact: Contact, iSaveListener: ContactsDataSource.ISaveListener?) {
        logcat("saveUserContacts")

        userContactsRef.child(contact.phoneNumber)
                .setValue(contact.toDataMap())
                .addOnFailureListener { iSaveListener!!.onError() }
                .addOnSuccessListener { iSaveListener!!.onSaved() }
    }

    // Update location permission
    override fun updatePermission(position: Int, number: Contact, iListener: ContactsDataSource.IResultListener?) {
        logcat("updatePermission")

        userContactsRef.child(number.phoneNumber)
                .child("allow").setValue(number.allow)
                .addOnFailureListener { iListener!!.onError(position) }
                .addOnSuccessListener { iListener!!.onSuccess(position) }
    }

    // Update user location
    override fun updateUserLocation(latitude: Double, longitude: Double) {
        logcat("updateUserLocation")

        val data = hashMapOf<String, Any>()
        data.put("lat", latitude)
        data.put("lng", longitude)

        usersLocationRef.child(userPhone)
                .updateChildren(data)
                .addOnFailureListener { logcat("update fail.") }
                .addOnSuccessListener { logcat("update success.") }
    }
}