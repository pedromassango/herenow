package com.pedromassango.herenow.data.local

import com.pedromassango.herenow.data.ContactsDataSource
import com.pedromassango.herenow.data.model.Contact

/**
 * Created by pedromassango on 12/29/17.
 */
class ContactsLocalRepository : ContactsDataSource {

    //TODO: save data localy

    companion object {
        private var INSTANCE: ContactsLocalRepository? = null

        fun getInstance(): ContactsLocalRepository {
            if (INSTANCE == null) {
                INSTANCE = ContactsLocalRepository()
            }
            return INSTANCE!!
        }
    }

    // Ignored on this class, we do not need to save user location localy
    override fun updateUserLocation(latitude: Double, longitude: Double) {}

    override fun saveUserContacts(contact: Contact, iSaveListener: ContactsDataSource.ISaveListener?) {
        iSaveListener?.onSaved()
    }

    override fun keepFriendsLocationSync(iLocationListener: ContactsDataSource.ILocationListener) {

    }

    override fun updatePermission(position: Int, number: Contact, iListener: ContactsDataSource.IResultListener?) {

    }

    override fun getContacts(iListener: ContactsDataSource.IListener<Contact>?) {
        val arrayList: ArrayList<Contact> = ArrayList()

        arrayList.add(Contact(contactName = "Pedro Massango", phoneNumber = "948 020 308"))
        arrayList.add(Contact(contactName = "Anisio Isidoro", phoneNumber = "923 123 463"))
        arrayList.add(Contact(contactName = "Mendes Massango", phoneNumber = "910 527 624"))
        arrayList.add(Contact(contactName = "Jose Eduardo", phoneNumber = "948 020 308"))
        arrayList.add(Contact(contactName = "Suraia Gourgel", phoneNumber = "928 573 178"))

        //iListener?.onSuccess(arrayList)
        iListener?.onError()
    }
}