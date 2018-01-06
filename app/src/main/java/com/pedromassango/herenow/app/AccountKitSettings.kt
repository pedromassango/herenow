package com.pedromassango.herenow.app

import android.content.Context
import com.facebook.accountkit.ui.*
import com.pedromassango.herenow.R

/**
 * Created by pedromassango on 12/28/17.
 */
object AccountKitSettings {

    private var output: AccountKitConfiguration.AccountKitConfigurationBuilder? = null
    private var uiManager: UIManager? = null

    operator fun get(context: Context): AccountKitConfiguration.AccountKitConfigurationBuilder {
        if (output == null) {
            output = AccountKitConfiguration.AccountKitConfigurationBuilder(
                    LoginType.PHONE,
                    AccountKitActivity.ResponseType.TOKEN) // or .ResponseType.TOKEN

            uiManager = SkinManager(
                    SkinManager.Skin.TRANSLUCENT,
                    context.resources.getColor(R.color.colorPrimary),
                    R.drawable.application_bacground,
                    SkinManager.Tint.BLACK, 55.0)

            // ... perform additional configuration ...
            output!!.setReceiveSMS(true)
            output!!.setFacebookNotificationsEnabled(true)
            output!!.setUIManager(uiManager)
        }

        return output!!
    }

    fun build(context: Context): AccountKitConfiguration {
        return get(context).build()
    }
}