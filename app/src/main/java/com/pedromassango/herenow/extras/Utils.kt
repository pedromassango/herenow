package com.pedromassango.herenow.extras

import android.content.Context
import android.net.ConnectivityManager

import com.pedromassango.herenow.app.HereNow

/**
 * Created by pedromassango on 12/26/17.
 */

object Utils {

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo.isConnected
    }
}
