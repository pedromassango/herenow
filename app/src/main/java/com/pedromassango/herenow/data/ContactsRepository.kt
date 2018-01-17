package com.pedromassango.herenow.data

import com.pedromassango.herenow.data.local.ContactsLocalRepository
import com.pedromassango.herenow.data.model.Contact
import com.pedromassango.herenow.data.remote.ContactsRemoteRepository

/**
 * Created by pedromassango on 12/30/17.
 */
class ContactsRepository(private val contactsRemoteRepository: ContactsRemoteRepository,
                         private val contactsLocalRepository: ContactsLocalRepository) : ContactsDataSource {

    companion object {
        private var INSTANCE: ContactsRepository? = null

        fun getInstance(contactsRemoteRepository: ContactsRemoteRepository,
                        contactsLocalRepository: ContactsLocalRepository): ContactsRepository {
            if (INSTANCE == null) {
                INSTANCE = ContactsRepository(contactsRemoteRepository, contactsLocalRepository)
            }
            return INSTANCE!!
        }
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

                // Update local repository
                contactsLocalRepository.updatePermission(position, number, null)

                // Notify the listener
                iListener!!.onSuccess(position)
            }

            override fun onError(position: Int) {
                iListener!!.onError(position)
            }
        })
    }

    override fun getContacts(iListener: ContactsDataSource.IListener<Contact>?) {
        contactsLocalRepository.getContacts(object : ContactsDataSource.IListener<Contact> {
            override fun onSuccess(data: ArrayList<Contact>) {
                if (data.isEmpty()) {
                    getFromRemote(iListener, true)
                    return
                }

                // Notify the listener
                iListener!!.onSuccess(data)

                // Get recent data from remote
                getFromRemote(iListener, false)
            }

            override fun onError() {
                getFromRemote(iListener, true)
            }
        })
    }

    private fun getFromRemote(iListener: ContactsDataSource.IListener<Contact>?, notifyError: Boolean) {
        contactsRemoteRepository.getContacts(object : ContactsDataSource.IListener<Contact> {
            override fun onSuccess(data: ArrayList<Contact>) {

                // update local repository
                data.forEach { contactsLocalRepository.saveUserContacts(it, null) }

                // Notify listener
                iListener!!.onSuccess(data)
            }

            override fun onError() {
                if (notifyError) {
                    iListener!!.onError()
                }
            }
        })
    }

    override fun saveUserContacts(contact: Contact, iSaveListener: ContactsDataSource.ISaveListener?) {
        contactsRemoteRepository.saveUserContacts(contact, object : ContactsDataSource.ISaveListener {
            override fun onSaved() {
                contactsLocalRepository.saveUserContacts(contact, null)
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
                contactsLocalRepository.removeContact(contact, position, null)
                iResultListener!!.onSuccess(position)
            }

            override fun onError(position: Int) = iResultListener!!.onError(position)
        })
    }
}