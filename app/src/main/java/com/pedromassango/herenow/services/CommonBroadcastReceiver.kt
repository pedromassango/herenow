package com.pedromassango.herenow.services

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.StringRes
import com.pedromassango.herenow.R
import com.pedromassango.herenow.data.preferences.PreferencesHelper

/**
 * Created by Pedro Massango on 1/12/18.
 *
 * A broadcast to show a application info with a popup inside a current activity.
 */
class CommonBroadcastReceiver : BroadcastReceiver() {

    companion object {
        // Action to show popup message
        const val SHOW_POPUP_MESSAGE = "com.pedromassango.herenow.services.CommonBroadcastReceiver.SHOW_POPUP_MESSAGE"
        const val SHOW_NOTIFICATION = "com.pedromassango.herenow.services.CommonBroadcastReceiver.SHOW_NOTIFICATION"
        const val INTENT_TITLE = "com.pedromassango.herenow.services.CommonBroadcastReceiver.INTENT_TITLE"
        const val INTENT_MESSAGE = "com.pedromassango.herenow.services.CommonBroadcastReceiver.INTENT_MESSAGE"
        const val INTENT_BOOLEAN_MESSAGE = "com.pedromassango.herenow.services.CommonBroadcastReceiver.INETNT_BOOLEAN_MESSAGE"

        // To be notified when need to show a popup via broadcastReceiver
        var iShowPopupListener: IShowPopupListener? = null
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        when (intent!!.action) {
        // Show Popup intent
            SHOW_POPUP_MESSAGE -> {
                val message = intent.getIntExtra(INTENT_MESSAGE, R.string.something_was_wrong)
                val closeOnClick = intent.getBooleanExtra(INTENT_BOOLEAN_MESSAGE, true)
                // Notify the activity that is listening from this listener
                iShowPopupListener?.onBroadcastShowPopup(message, closeOnClick)
            }
            SHOW_NOTIFICATION -> {
                val title = intent.getStringExtra(INTENT_TITLE)
                checkNotNull(title)
                val message = intent.getStringExtra(INTENT_MESSAGE)
                checkNotNull(message)

                // Check if the user allow notification no Settings
                val allowedNotifications = PreferencesHelper(context!!).showNotification

                // If user allowed notifications, show it
                if (allowedNotifications) {
                    showNotification(context, title, message)
                }
            }
        }

    }

    interface IShowPopupListener {
        fun onBroadcastShowPopup(@StringRes message: Int, closeOnClick: Boolean)
    }

    private fun showNotification(context: Context, title: String, message: String) {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, context.getString(R.string.app_name))
        } else {
            Notification.Builder(context)
        }

        builder.setAutoCancel(true)
        builder.setSmallIcon(R.mipmap.ic_launcher)

        // Content
        builder.setContentTitle(title)
        builder.setContentText(message)

        //TODO: testing info notification type
        //builder.setOnlyAlertOnce(true)

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(289, builder.build())
    }

}