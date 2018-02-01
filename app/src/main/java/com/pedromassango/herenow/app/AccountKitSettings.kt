package com.pedromassango.herenow.app

import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType

/**
 * Created by pedromassango on 12/28/17.
 * Facebook Account Kit settings.
 */
object AccountKitSettings {

    private var output: AccountKitConfiguration.AccountKitConfigurationBuilder? = null

    fun get(): AccountKitConfiguration.AccountKitConfigurationBuilder {
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

    fun build(): AccountKitConfiguration {
        return get().build()
    }
}