package com.pedromassango.herenow.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.annotation.StringRes
import com.pedromassango.herenow.R

/**
 * Created by Pedro Massango on 1/12/18.
 *
 * A broadcast to show a application info with a popup inside a current activity.
 */
class PopupBroadcastReceiver : BroadcastReceiver() {

    companion object {
        // Action to show popup message
        val SHOW_POPUP_MESSAGE = "com.pedromassango.herenow.services.PopupBroadcastReceiver.SHOW_POPUP_MESSAGE"
        val INTENT_MESSAGE = "INTENT_MESSAGE"
        val INTENT_BOOLEAN_MESSAGE = "INETNT_BOOLEAN_MESSAGE"

        // To be notified when need to show a popup via broadcastReceiver
        var iShowPopupListener: IShowPopupListener? = null
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        when(intent!!.action){
            // Show Popup intent
            SHOW_POPUP_MESSAGE -> {
                val message = intent.getIntExtra(INTENT_MESSAGE, R.string.something_was_wrong)
                val closeOnClick = intent.getBooleanExtra(INTENT_BOOLEAN_MESSAGE, true)
                // Notify the activity that is listening from this listener
                iShowPopupListener?.onBroadcastShowPopup(message, closeOnClick)
            }
        }

    }

    interface IShowPopupListener{
        fun onBroadcastShowPopup(@StringRes message: Int, closeOnClick: Boolean)
    }
}