package com.pedromassango.herenow.services

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.pedromassango.herenow.extras.Utils

/**
 * Created by pedromassango on 1/5/18.
 *
 * This BroadCast notify the MainActivity when the device internet connection as changed.
 */
open class NetworkBroadcastReceiver : BroadcastReceiver() {

    companion object {
        // A static var
        var iConnectionListener: IConnectionListener? = null
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {

        // Get connection state
        val state = Utils.isConnected(context!!)

        // Notify MainActivity if {iConnectionListener} is not null
        iConnectionListener?.onConnectionChanged(state)
    }

    /**
     * Used to notify MainActivity for connection changes.
     */
    interface IConnectionListener {
        fun onConnectionChanged(connected: Boolean)
    }
}