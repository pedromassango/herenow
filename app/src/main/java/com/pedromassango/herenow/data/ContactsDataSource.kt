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

    interface IListener<T>{
        fun onSuccess(data: ArrayList<T>)
        fun onError()
    }

    interface IResult<T>{
        fun onSuccess(data: ArrayList<T>)
    }

    interface ILocationListener{
        fun onAllowed(contact: ArrayList<Contact>)
        fun onNoFriends()
        fun onError()
    }

    interface IContactListener{
        fun onSuccess(contact: Contact?)
    }

    interface IResultListener{
        fun onSuccess(position: Int)
        fun onError(position: Int)
    }

    fun updateUserLocation(latitude: Double, longitude: Double)

    fun getContacts(iListener: IListener<Contact>?)

    fun updatePermission(position: Int, number:Contact, iListener: IResultListener?)

    fun saveUserContacts(contact: Contact, iSaveListener: ISaveListener?)

    fun keepFriendsLocationSync(iLocationListener: ILocationListener)
}