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
        fun showContacts(data: ArrayList<Contact>)
    }

    interface Presenter {
        fun contactPicked(contact: Contact)

    }
}