package com.pedromassango.herenow.ui.main.fragments.contacts

import android.support.v7.widget.RecyclerView
import android.view.View
import com.pedromassango.herenow.data.model.Contact
import kotlinx.android.synthetic.main.row_contact.view.*

/**
 * Created by pedromassango on 12/29/17.
 */
class ContactVH(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindViews(contact: Contact) {
        val name = contact.contactName
        val number = contact.phoneNumber
        //TODO: handle checkBox permission
        val canSee = !number.isEmpty()

        with(view) {
            //TODO: show image if have
            tv_contact_first_letter.text = contact.getSimpleName()
            tv_contact_number.text = number
            tv_contact_name.text = if (name.isEmpty()) number.also { tv_contact_number.visibility = View.GONE } else name

            cb_contact_permission.isChecked = canSee
        }
    }
}