package com.pedromassango.herenow.extras

import android.content.Context
import android.content.Intent
import android.support.annotation.StringRes
import com.pedromassango.herenow.services.PopupBroadcastReceiver

/**
 * Created by Pedro Massango on 1/12/18.
 */
object ActivityUtils {

    /**
     * Send the broadcast to show popup message on current activity.
     */
    fun showPopupMessage(context: Context, @StringRes message: Int, closeOnClick: Boolean = true){
        val popupBroadcastIntent = Intent(context, PopupBroadcastReceiver::class.java)
        popupBroadcastIntent.action = PopupBroadcastReceiver.SHOW_POPUP_MESSAGE
        popupBroadcastIntent.putExtra(PopupBroadcastReceiver.INTENT_BOOLEAN_MESSAGE, closeOnClick)
        popupBroadcastIntent.putExtra(PopupBroadcastReceiver.INTENT_MESSAGE, message)

        context.sendBroadcast(popupBroadcastIntent)
    }
}