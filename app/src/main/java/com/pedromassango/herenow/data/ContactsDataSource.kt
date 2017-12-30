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

    interface IGetListener<T>{
        fun onSuccess(data: ArrayList<T>)
        fun onError()
    }

    fun getContacts(iGetListener: IGetListener<Contact>?)

    fun saveUserContacts(contact: Contact, iSaveListener: ISaveListener?)
}