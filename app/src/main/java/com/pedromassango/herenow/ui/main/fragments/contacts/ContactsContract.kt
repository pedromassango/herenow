package com.pedromassango.herenow.ui.main.fragments.contacts

import com.pedromassango.herenow.data.model.Contact

/**
 * Created by pedromassango on 12/29/17.
 */
class ContactsContract {

    companion object {

        val RESULT_CONTACT_PICKER = 743
    }

    interface View {
        fun showContact(data: Contact)
        fun showContact(data: ArrayList<Contact>)
        fun showSaveContactProgress()
        fun dismissSaveContactProgress()
        fun showSaveContactError()
        val isConnected: Boolean
        fun showNoInternetInfo()
        fun showGetContactsProgress()
        fun showNoContacts()
        fun showGetContactsError()
        fun showPermissionUpdateSuccess()
        fun updateContactInAdapter(position: Int, contact: Contact)
        fun showPleaseWaitMessage()
    }

    interface Presenter {
        fun contactPicked(contact: Contact)
        fun getUserContacts()
        fun contactPerssionSwitched(mPosition: Int, contact: Contact)
    }
}