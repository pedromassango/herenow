package com.pedromassango.herenow.extras

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by pedromassango on 12/26/17.
 */

object Utils {

    /**
     * Build the function only if the version is >= android N
     */
    /*@RequiresApi(Build.VERSION_CODES.M)
    val supportLoolipop = {minVersion: Int, code: () -> Unit ->
        if(Build.VERSION.SDK_INT >= minVersion){
            code()
        }
    }*/

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
                    .replace("+", "") // remove +
                    .replace(" ", "") // remove blank space
}
