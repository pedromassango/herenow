package com.pedromassango.herenow.extras

import android.content.Context
import android.content.Intent
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.pedromassango.herenow.R
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
     * Replace fragments on activity
     */
    fun replaceFragment(supportFragmentManager: FragmentManager,
        frame_layout_id: Int = R.id.frame_layout,
        fragment: Fragment){

        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)

        // Show the selected frament
        transaction.replace(frame_layout_id, fragment, fragment::class.java.simpleName)
                .commit()
    }

    /**
     * Send a broadcast to show a notification
     */
    fun showNotification(context: Context, title: String, message: String){
        TODO("Method not implemented yet")
    }
}