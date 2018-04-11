package com.pedromassango.herenow.data

import com.pedromassango.herenow.data.model.Contact
import com.pedromassango.herenow.data.model.Notification
import com.pedromassango.herenow.data.remote.ContactsRemoteRepository

/**
 * Created by pedromassango on 12/30/17.
 */
class ContactsRepository(private val contactsRemoteRepository: ContactsRemoteRepository) : ContactsDataSource {

    companion object {
        private var INSTANCE: ContactsRepository? = null

        fun getInstance(contactsRemoteRepository: ContactsRemoteRepository): ContactsRepository {
            if (INSTANCE == null) {
                INSTANCE = ContactsRepository(contactsRemoteRepository)
            }
            return INSTANCE!!
        }
    }

    override fun sendNotification(update: Boolean, notification: Notification) {
        contactsRemoteRepository.sendNotification(update, notification)
    }

    override fun getNotifications(callback: (ArrayList<Notification>) -> Unit) {
        contactsRemoteRepository.getNotifications(callback)
    }

    override fun keepFriendsLocationSync(iLocationListener: ContactsDataSource.ILocationListener) {
        contactsRemoteRepository.keepFriendsLocationSync(iLocationListener)
    }

    override fun updateUserLocation(latitude: Double, longitude: Double) {

        // Update location only remotely
        contactsRemoteRepository.updateUserLocation(latitude, longitude)
    }

    override fun updatePermission(position: Int, number: Contact, iListener: ContactsDataSource.IResultListener?) {
        contactsRemoteRepository.updatePermission(position, number, object : ContactsDataSource.IResultListener {
            override fun onSuccess(position: Int) {

                // Notify the listener
                iListener!!.onSuccess(position)
            }

            override fun onError(position: Int) {
                iListener!!.onError(position)
            }
        })
    }

    override fun getContacts(iListener: ContactsDataSource.IListener<Contact>?) {
        contactsRemoteRepository.getContacts(object : ContactsDataSource.IListener<Contact> {
            override fun onSuccess(data: MutableList<Contact>) {

                // Notify listener
                iListener!!.onSuccess(data)
            }

            override fun onError() {
                iListener!!.onError()
            }
        })
    }

    override fun saveUserContacts(contact: Contact, iSaveListener: ContactsDataSource.ISaveListener?) {
        contactsRemoteRepository.saveUserContacts(contact, object : ContactsDataSource.ISaveListener {
            override fun onSaved() {

                // Send a notification to thr saved contact, and notify the listener
                iSaveListener!!.onSaved()
            }

            override fun onError() {
                iSaveListener!!.onError()
            }
        })
    }

    override fun removeContact(contact: Contact, position: Int, iResultListener: ContactsDataSource.IResultListener?) {
        contactsRemoteRepository.removeContact(contact, position, object : ContactsDataSource.IResultListener {
            override fun onSuccess(position: Int) {
                iResultListener!!.onSuccess(position)
            }

            override fun onError(position: Int) = iResultListener!!.onError(position)
        })
    }
}