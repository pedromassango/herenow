package com.pedromassango.herenow.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.pedromassango.herenow.extras.Utils

/**
 * Created by pedromassango on 1/5/18.
 */
open class MyBroadcastReceiver : BroadcastReceiver() {

    companion object {
        var iConnectionListener: IConnectionListener? = null
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Broadcast", Toast.LENGTH_SHORT).show()

        val state = Utils.isConnected(context!!)
        iConnectionListener?.onConnectionChanged(state)
    }

    interface IConnectionListener {
        fun onConnectionChanged(connected: Boolean)
    }
}