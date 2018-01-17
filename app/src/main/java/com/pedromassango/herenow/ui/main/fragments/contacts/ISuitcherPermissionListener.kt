package com.pedromassango.herenow.ui.main.fragments.contacts

import com.pedromassango.herenow.data.model.Contact

/**
 * Created by pedromassango on 1/2/18.
 */
/**
 * Listener to listen when a permission switcher was clicked in ContactsFragment.
 */
interface ISuitcherPermissionListener {

    operator fun invoke(position: Int, contact: Contact)
}