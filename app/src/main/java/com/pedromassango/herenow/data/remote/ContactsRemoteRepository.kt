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

        // Get from User friends list
        getContacts(object : ContactsDataSource.IListener<Contact> {
            override fun onSuccess(data: ArrayList<Contact>) {
                logcat("keepFriendsLocationSync: getContacts -> done: ${data.size}")

                // Check if the user have friends
                if (data.isEmpty()) {
                    iLocationListener.onNoFriends();
                    return
                }

                logcat("keepFriendsLocationSync: getContacts -> checking authorization...")

                // CHECKING AUTHORIZATION && GET FRIENDS LOCATION if allowed
                // Check if the user is on their friend allowed list
                // For each friend, check if we can see their location
                checkAuthorization(data, iLocationListener)

            }

            override fun onError() = iLocationListener.onError()
        })
    }

    /**
     * This method check if the user is on some friend list, and
     * retrieve then if was found.
     * @param friendNumber the friend number to check in
     * @param userNumber the current user number to check if is on list
     * @param iContactListener the listener to retrieve the contact
     * @return the friend checked.
     */
    private fun checkAuthorization(friends: ArrayList<Contact>,
                                   iLocationListener: ContactsDataSource.ILocationListener) {

        val friendsSize = friends.size
        var friendsVerifies = 0
        val checkedFirends = arrayListOf<Contact?>()

        // For each friend, check if we can see their location
        friends.forEach { friend ->
            rootRef.child(USERS_CONTACTS)
                    .child(friend.phoneNumber)
                    .child(userPhone)
                    .addValueEventListener(IValueEventListener(object : MyValueListener {
                        override fun success(contact: Contact?) {
                            // Increase friends verifieds field
                            friendsVerifies++

                            // Add this friend to result list if this friend allow you to see their location
                            when (contact != null && contact.allow) {
                                true -> checkedFirends.add(friend)
                                else -> checkedFirends.add(null)
                            }

                            // If has verified all friends, do the next thing
                            if (friendsSize == friendsVerifies) {
                                // Get only friends who allowed this user to see their location
                                val tempList = checkedFirends.filterNotNull()
                                val friendsWhoAllowedToSeeTheirLocations = arrayListOf<Contact>()
                                friendsWhoAllowedToSeeTheirLocations.addAll(tempList)

                                if (friendsWhoAllowedToSeeTheirLocations.isNotEmpty()) {
                                    logcat("Friends allowed you to see their locations")

                                    logcat("Getting friends location or Nothing...")

                                    //GETTING LOCATION
                                    // User is allowed to see location, get friend location
                                    getFriendLocation(friendsWhoAllowedToSeeTheirLocations, object : ContactsDataSource.IResult<Contact> {
                                        override fun onSuccess(data: ArrayList<Contact>) {
                                            logcat("Friend location received")
                                            iLocationListener.onAllowed(data)
                                        }
                                    })
                                } else {  // There is no friends tha allow user to see their location
                                    iLocationListener.onNoFriends()
                                }

                            }
                        }
                    }))
        }
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

    fun getFriendLocation(friends: ArrayList<Contact>, iResult: ContactsDataSource.IResult<Contact>) {

        val tempList = arrayListOf<Contact>()

        friends.forEach { friend ->
            usersLocationRef.child(friend.phoneNumber)
                    .addValueEventListener(IValueEventListener(object : MyLocationListener {
                        override fun success(p0: DataSnapshot?) {
                            checkNotNull(p0)
                            logcat("getFriendLocation: SHOT s-> $p0")

                            // Get latitude and longitude from giving snapshot
                            val latitude = p0!!.child("lat").value as Double
                            val longitude = p0.child("lng").value as Double

                            logcat("friend ${friend.phoneNumber}")
                            logcat("LAT: $latitude LNG: $longitude")

                            // Pass the friend location
                            friend.lat = latitude
                            friend.lng = longitude

                            // save the friend with their location on temporary list
                            tempList.add(friend)

                            // Check if retrieve location for all friends
                            if (tempList.size == friends.size) {
                                iResult.onSuccess(tempList)
                            }
                        }
                    }))
        }

    }
}