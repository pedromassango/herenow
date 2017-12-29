package com.pedromassango.herenow.data

import com.pedromassango.herenow.data.model.Contact

/**
 * Created by pedromassango on 12/29/17.
 */
interface ContactsDataSource{

    interface ISaveListener{
        fun onSaved()
        fun onError()
    }

    fun saveUserContacts(contact: Contact, iSaveListener: ISaveListener)
}