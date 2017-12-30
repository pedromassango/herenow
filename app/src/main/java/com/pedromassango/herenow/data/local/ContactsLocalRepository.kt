package com.pedromassango.herenow.data.local

import com.pedromassango.herenow.data.ContactsDataSource
import com.pedromassango.herenow.data.model.Contact

/**
 * Created by pedromassango on 12/29/17.
 */
class ContactsLocalRepository : ContactsDataSource {

    companion object {
        private var INSTANCE: ContactsLocalRepository? = null

        fun getInstance(): ContactsLocalRepository {
            if(INSTANCE == null){
                INSTANCE = ContactsLocalRepository()
            }
            return INSTANCE!!
        }
    }

    override fun saveUserContacts(contact: Contact, iSaveListener: ContactsDataSource.ISaveListener?) {
        iSaveListener?.onSaved()
    }

    override fun getContacts(iGetListener: ContactsDataSource.IGetListener<Contact>?) {
        var DUMMY: ArrayList<Contact> = ArrayList()

            DUMMY.add(Contact(contactName = "Pedro Massango", phoneNumber = "948 020 308"))
            DUMMY.add(Contact(contactName = "Anisio Isidoro", phoneNumber = "923 123 463"))
            DUMMY.add(Contact(contactName = "Mendes Massango", phoneNumber = "910 527 624"))
            DUMMY.add(Contact(contactName = "Pedro Massango", phoneNumber = "948 020 308"))
            DUMMY.add(Contact(contactName = "Suraia Gourgel", phoneNumber = "928 573 178"))
            DUMMY.add(Contact(contactName = "Anisio Isidoro", phoneNumber = "923 123 463"))

        iGetListener?.onSuccess(DUMMY)
    }
}