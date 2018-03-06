package com.pedromassango.herenow.ui.main.fragments.contacts

import com.pedromassango.herenow.app.HereNow
import com.pedromassango.herenow.app.HereNow.Companion.logcat
import com.pedromassango.herenow.data.ContactsDataSource
import com.pedromassango.herenow.data.ContactsRepository
import com.pedromassango.herenow.data.model.Contact
import com.pedromassango.herenow.data.preferences.PreferencesHelper
import com.pedromassango.herenow.extras.Constants
import com.pedromassango.herenow.extras.NotificationSender
import com.pedromassango.herenow.extras.Utils

/**
 * Created by pedromassango on 12/29/17.
 */
class ContactsPresenter(private val view: ContactsContract.View,
                        private val contactsRepository: ContactsRepository,
                        private val preferencesHelper: PreferencesHelper) : ContactsContract.Presenter {

    override fun getUserContacts() {
        HereNow.logcat("ContactsPresenter: getUserContacts..")
        if (!view.isConnected && !preferencesHelper.dataLoaded) {
            view.showGetContactsError()
            return
        }

        // Show progress
        view.showGetContactsProgress()

        // Get contacts
        contactsRepository.getContacts(object : ContactsDataSource.IListener<Contact> {
            override fun onSuccess(data: MutableList<Contact>) {
                HereNow.logcat("ContactsPresenter: getUserContacts - onSuccess")

                // check if there have contacts
                if (data.isEmpty()) {
                    view.showNoContacts()
                    return
                }

                // Save state that first data loader as succeded.
                preferencesHelper.dataLoaded = true

                view.showContact(data)
            }

            override fun onError() {
                HereNow.logcat("ContactsPresenter: getUserContacts - onError")
                view.showGetContactsError()
            }
        })
    }

    override fun contactPicked(contact: Contact) {
        logcat("PICKED: $contact")

        // Check if there is connection to internet
        val isConnected = view.isConnected

        // Show progress while saving ONLY if there is internet connection
        // otherwise just save it, and reload the data.
        if(isConnected) {
            view.showSaveContactProgress()
        }

        // Format phone number before save
        contact.phoneNumber = Utils.getFormatedNumber(contact.phoneNumber)

        // Save the contact
        contactsRepository.saveUserContacts(contact, object : ContactsDataSource.ISaveListener {
            override fun onSaved() {

                // Notify the saved user that someone added him to a contacts list.
                NotificationSender.send(
                        Constants.NOTIFICATION_TYPE_ADDED_AS_FRIEND,
                        contact.phoneNumber,
                        preferencesHelper.phoneNumber)

                view.dismissSaveContactProgress()
                view.showContact(contact)
            }

            override fun onError() {
                view.dismissSaveContactProgress()

                // Show save contact error
                view.showSaveContactError()
            }
        })

        // if is not connected, add the data manually, because Firebase never notify
        // listeners when the device is offline.
        if(!isConnected){
            view.showContact( contact)
        }
    }

    override fun contactPermissionSwitched(mPosition: Int, contact: Contact) {
        logcat("contactPermissionSwitched: $mPosition")

        // Check if is connected, before execute task
        if (!view.isConnected) {
            return
        }

        // Show a feedback, that the action is being processed
        view.showPleaseWaitMessage()

        // Request permissions updates on Database
        contactsRepository.updatePermission(mPosition, contact, object : ContactsDataSource.IResultListener {
            override fun onError(position: Int) {
                logcat("onError: $position")

                // Revert changes on Adapter
                contact.allow = !contact.allow

                view.updateContactInAdapter(position, contact)
            }

            override fun onSuccess(position: Int) {
                logcat("onSuccess: $position")

                // Update item in adapter
                view.updateContactInAdapter(position, contact)

                view.showPermissionUpdateSuccess()
            }
        })
    }

    override fun onDeleteContact(contact: Contact, position: Int) {

        contactsRepository.removeContact(contact, position, object : ContactsDataSource.IResultListener {
            override fun onSuccess(position: Int) {

                view.showContactDeletedMessage()
            }

            override fun onError(position: Int) {

                view.showDeleteErrorMessage()
                view.showContact(contact)
            }
        })
    }
}