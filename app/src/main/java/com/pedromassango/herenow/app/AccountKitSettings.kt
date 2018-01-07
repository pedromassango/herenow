package com.pedromassango.herenow.app

import android.content.Context
import com.facebook.accountkit.ui.*
import com.pedromassango.herenow.R

/**
 * Created by pedromassango on 12/28/17.
 */
object AccountKitSettings {

    private var output: AccountKitConfiguration.AccountKitConfigurationBuilder? = null

    operator fun get(context: Context): AccountKitConfiguration.AccountKitConfigurationBuilder {
        if (output == null) {
            output = AccountKitConfiguration.AccountKitConfigurationBuilder(
                    LoginType.PHONE,
                    AccountKitActivity.ResponseType.TOKEN) // or .ResponseType.TOKEN

            // ... perform additional configuration ...
            output!!.setReceiveSMS(true)
            output!!.setFacebookNotificationsEnabled(true)
        }

        return output!!
    }

    fun build(context: Context): AccountKitConfiguration {
        return get(context).build()
    }
}