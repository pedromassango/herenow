package com.pedromassango.herenow.extras

import android.content.Context
import android.net.ConnectivityManager
import com.pedromassango.herenow.BuildConfig

import com.pedromassango.herenow.app.HereNow

/**
 * Created by pedromassango on 12/26/17.
 */

object Utils {

    fun isConnected(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            cm.activeNetworkInfo?.isConnected ?: false
        } catch (ex: NullPointerException) {
            ex.printStackTrace()
            false
        }
    }

    fun getFormatedNumber(number: String) =
            number.replace("-", "") // remove -
                    .replace(" ", "") // remove blank space
}
