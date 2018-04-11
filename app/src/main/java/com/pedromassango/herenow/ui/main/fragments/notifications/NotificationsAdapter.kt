package com.pedromassango.herenow.ui.main.fragments.notifications

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pedromassango.herenow.R
import com.pedromassango.herenow.data.model.Notification

/**
 * Created by pedromassango on 12/29/17.
 */
class NotificationsAdapter(private val context: Context,
                           private var iClickListener: (position: Int, notification: Notification, allow: Boolean)-> Unit) : RecyclerView.Adapter<NotificationVH>() {

    private var contacts = arrayListOf<Notification>()

    fun add(n: Notification) {
        synchronized(NotificationsAdapter::class.java) {
            contacts.add(n)
            notifyDataSetChanged()
        }
    }

    fun add(notifications: ArrayList<Notification>) {
        synchronized(NotificationsAdapter::class.java) {
            contacts.clear()
            contacts.addAll(notifications)
            notifyDataSetChanged()
        }
    }

    fun update(position: Int, notification: Notification) {
        synchronized(NotificationsAdapter::class.java) {
            contacts.removeAt(position)
            contacts.add(position, notification)
            notifyItemChanged(position, notification)
        }
    }

    override fun onBindViewHolder(holder: NotificationVH?, position: Int) {
        val n = contacts[position]
        holder!!.bindViews(n, iClickListener)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NotificationVH {
        val view = LayoutInflater.from(context).inflate(R.layout.row_notification, parent, false)
        return NotificationVH(view)
    }

    fun removeAt(adapterPosition: Int) {
        contacts.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }

    fun getItem(adapterPosition: Int): Notification = contacts[adapterPosition]
}