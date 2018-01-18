package com.pedromassango.herenow.extras

import android.content.Context
import android.content.Intent
import android.support.annotation.StringRes
import com.pedromassango.herenow.services.CommonBroadcastReceiver

/**
 * Created by Pedro Massango on 1/12/18.
 */
object ActivityUtils {

    /**
     * Send the broadcast to show popup message on current activity.
     */
    fun showPopupMessage(context: Context, @StringRes message: Int, closeOnClick: Boolean = true){
        val popupBroadcastIntent = Intent(context, CommonBroadcastReceiver::class.java)
        popupBroadcastIntent.action = CommonBroadcastReceiver.SHOW_POPUP_MESSAGE
        popupBroadcastIntent.putExtra(CommonBroadcastReceiver.INTENT_BOOLEAN_MESSAGE, closeOnClick)
        popupBroadcastIntent.putExtra(CommonBroadcastReceiver.INTENT_MESSAGE, message)

        context.sendBroadcast(popupBroadcastIntent)
    }

    /**
     * Send a broadcast to show a notification
     */
    fun showNotification(context: Context, title: String, message: String){
        TODO("Method not implemented yet")
    }
}