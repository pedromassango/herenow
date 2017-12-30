package com.pedromassango.herenow.data.remote

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pedromassango.herenow.data.ContactsDataSource
import com.pedromassango.herenow.data.preferences.PreferencesHelper
import com.pedromassango.herenow.data.model.Contact

/**
 * Created by pedromassango on 12/29/17.
 */
class ContactsRemoteRepository(private val preferencesHelper: PreferencesHelper) : ContactsDataSource {

    private val USERS_CONTACTS = "users_contacts"
    private val USERS_I_CAN_SEE = "users_i_can_see"
    private val USERS_I_CAN_NOT_SEE = "users_i_cant_see"
    private val USERS_LOCATION = "users_location"

    companion object {
        private var INSTANCE: ContactsRemoteRepository? = null

        fun getInstance(preferencesHelper: PreferencesHelper): ContactsRemoteRepository {
            if(INSTANCE == null){
                INSTANCE = ContactsRemoteRepository(preferencesHelper = preferencesHelper)
            }
            return INSTANCE!!
        }
    }

    private val rootRef = FirebaseDatabase.getInstance().reference

    // USERS_DATA
    private val usersContactsRef = rootRef.child(USERS_CONTACTS).ref
    private val usersLocationRef = rootRef.child(USERS_LOCATION).ref
    private val usersICanSeeRef = rootRef.child(USERS_I_CAN_SEE).ref
    private val usersICantSeeRef = rootRef.child(USERS_I_CAN_NOT_SEE).ref

    // Current user phone number
    private val userPhone = preferencesHelper.phoneNumber

    // Read from USERS_CONTACTS
    override fun getContacts(iGetListener: ContactsDataSource.IGetListener<Contact>?) {

        usersContactsRef.child(userPhone)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?)  = iGetListener!!.onError()

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        if(!dataSnapshot!!.exists()) {
                            iGetListener!!.onSuccess(arrayListOf())  // No user contacts
                            return
                        }

                        // Temp list to add fetched items
                        val tempList = arrayListOf<Contact>()

                        dataSnapshot.children.forEach {
                        val contact = it.getValue(Contact::class.java)
                            tempList.add( contact!!)
                        }

                        // Notify the listener
                        iGetListener?.onSuccess( tempList)
                    }
                })

    }

    // Save in USERS_CONTACTS
    override fun saveUserContacts(contact: Contact, iSaveListener: ContactsDataSource.ISaveListener?) {
        usersContactsRef.child( userPhone)
                .child(contact.phoneNumber)
                .setValue( contact.toDataMap())
                .addOnFailureListener { iSaveListener!!.onError() }
                .addOnSuccessListener { iSaveListener!!.onSaved() }
    }
}