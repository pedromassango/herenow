package com.pedromassango.herenow.ui.main.fragments.contacts

import com.pedromassango.herenow.app.HereNow.Companion.logcat
import com.pedromassango.herenow.data.ContactsDataSource
import com.pedromassango.herenow.data.ContactsRepository
import com.pedromassango.herenow.data.model.Contact

/**
 * Created by pedromassango on 12/29/17.
 */
class ContactsPresenter(private val view: ContactsContract.View,
                        private val contactsRepository: ContactsRepository) : ContactsContract.Presenter {

    override fun getUserContacts() {

        /*if(!view.isConnected){
            view.showNoInternetInfo()
            return
        }*/

        // Show progress
        view.showGetContactsProgress()

        // Get contacts
        contactsRepository.getContacts(object : ContactsDataSource.IListener<Contact>{
            override fun onSuccess(data: ArrayList<Contact>) {

                if(data.isEmpty()){
                    view.showNoContacts()
                    return
                }

                view.showContact( data)
            }

            override fun onError() {
                view.showGetContactsError()
            }
        })
    }

    override fun contactPicked(contact: Contact) {
        logcat("PICKED: $contact")

        // Check if there is connection to internet
        if(!view.isConnected){
            view.showNoInternetInfo()
            return
        }

        // Show progress while saving
        view.showSaveContactProgress()

        // Save the contact
        contactsRepository.saveUserContacts(contact, object : ContactsDataSource.ISaveListener{
            override fun onSaved() {
                view.dismissSaveContactProgress()
                view.showContact(contact)
            }

            override fun onError() {
                view.dismissSaveContactProgress()

                // Show save contact error
                view.showSaveContactError()
            }
        })
    }

    override fun contactPerssionSwitched(mPosition: Int, contact: Contact) {
        logcat("contactPerssionSwitched: $mPosition")

        // Check if is connected, before execute task
        if(!view.isConnected){
            view.showNoInternetInfo()
            return
        }

        // Show a feedback, that the action is being processed
        view.showPleaseWaitMessage()

        contactsRepository.updatePermission(mPosition, contact, object : ContactsDataSource.IResultListener {
            override fun onError(position: Int) {
                logcat("onError: $position")

                // Revert changes on Adapter
                contact.allow = !contact.allow

                view.updateContactInAdapter(position, contact)
                view.showNoInternetInfo()
            }

            override fun onSuccess(position: Int) {
                logcat("onSuccess: $position")

                // Update item in adapter
                view.updateContactInAdapter( position, contact)

                view.showPermissionUpdateSuccess()
            }
        })
    }
}