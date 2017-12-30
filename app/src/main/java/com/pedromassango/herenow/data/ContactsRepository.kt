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

    override fun getContacts(iGetListener: ContactsDataSource.IGetListener<Contact>?) {
        contactsLocalRepository.getContacts(object : ContactsDataSource.IGetListener<Contact> {
            override fun onSuccess(data: ArrayList<Contact>) {
                if (data.isEmpty()) {
                    getFromRemote(iGetListener, true)
                    return
                }

                // Notify the listener
                iGetListener!!.onSuccess(data)

                // Get recent data from remote
                getFromRemote(iGetListener, false)
            }

            override fun onError() {
                getFromRemote(iGetListener, true)
            }
        })
    }

    private fun getFromRemote(iGetListener: ContactsDataSource.IGetListener<Contact>?, notifyError: Boolean) {
        contactsRemoteRepository.getContacts(object : ContactsDataSource.IGetListener<Contact> {
            override fun onSuccess(data: ArrayList<Contact>) {

                // update local repository
                data.forEach { contactsLocalRepository.saveUserContacts(it, null) }

                // Notify listener
                iGetListener!!.onSuccess(data)
            }

            override fun onError() {
                if (notifyError) {
                    iGetListener!!.onError()
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
}