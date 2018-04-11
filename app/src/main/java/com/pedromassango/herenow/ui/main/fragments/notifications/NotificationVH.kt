package com.pedromassango.herenow.ui.main.fragments.notifications

import android.support.v7.widget.RecyclerView
import android.view.View
import com.pedromassango.herenow.data.model.Notification
import kotlinx.android.synthetic.main.row_notification.view.*

/**
 * Created by pedromassango on 12/29/17.
 */
class NotificationVH(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindViews(contact: Notification, iClickListener: (position: Int, notification: Notification, allow: Boolean)-> Unit) {
        with(view) {
            with(contact) {

                tv_notification_author.text = getNameOrNumber()

                // If the user already allow to see their location, hide the buttons
                btn_allow.visibility = if(allowed) View.GONE else View.VISIBLE

                btn_allow.setOnClickListener {
                    iClickListener(adapterPosition, contact, true)
                }
            }
        }
    }
}