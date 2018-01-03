package com.pedromassango.herenow.ui.main.fragments.contacts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pedromassango.herenow.R
import com.pedromassango.herenow.data.model.Contact
import com.pedromassango.herenow.ui.main.ISuitcherPermissionListener

/**
 * Created by pedromassango on 12/29/17.
 */
class ContactAdapter(private val mContacts: ArrayList<Contact>,
                     private var switcherListener: ISuitcherPermissionListener) : RecyclerView.Adapter<ContactVH>() {

    private var contacts: ArrayList<Contact>

    init {
        contacts = mContacts
    }

    constructor(switcherListener: ISuitcherPermissionListener) : this(arrayListOf(), switcherListener) {
        contacts = arrayListOf()
        this.switcherListener = switcherListener
    }

    fun add(contact: Contact) {
        synchronized(ContactAdapter::class.java) {
            contacts.add(contact)
            notifyDataSetChanged()
        }
    }

    fun add(mContacts: ArrayList<Contact>) {
        synchronized(ContactAdapter::class.java) {
            contacts.clear()
            contacts.addAll(mContacts)
            notifyDataSetChanged()
        }
    }

    fun update(position: Int, contact: Contact) {
        synchronized(ContactAdapter::class.java) {
            contacts.removeAt(position)
            contacts.add(position, contact)
            notifyItemChanged(position)
        }
    }

    override fun onBindViewHolder(holder: ContactVH?, position: Int) {
        val contact = contacts[position]

        holder!!.bindViews(contact, switcherListener)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ContactVH {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.row_contact, parent, false)
        return ContactVH(view)
    }
}