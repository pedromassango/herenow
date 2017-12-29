package com.pedromassango.herenow.ui.main.fragments.contacts

import com.pedromassango.herenow.app.HereNow.Companion.logcat
import com.pedromassango.herenow.data.model.Contact

/**
 * Created by pedromassango on 12/29/17.
 */
class ContactsPresenter(private val view: ContactsContract.View) : ContactsContract.Presenter {

    override fun contactPicked(contact: Contact) {
        logcat("PICKED: $contact")

        //TODO: save picked contact
    }
}