package com.pedromassango.herenow.ui.login

import com.facebook.accountkit.Account
import com.facebook.accountkit.AccountKit
import com.facebook.accountkit.AccountKitCallback
import com.facebook.accountkit.AccountKitError
import com.pedromassango.herenow.app.HereNow.Companion.logcat
import com.pedromassango.herenow.data.PreferencesHelper

/**
 * Created by pedromassango on 12/28/17.
 */
class LoginPresenter(val view: LoginContract.View,
                     val preferencesHelper: PreferencesHelper) : LoginContract.Presenter {


    override fun startLoginRequest() {
        view.startAccountKitActivity()
    }

    override fun saveAccountInfo() {
        logcat("saveAccountInfo...")

        AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {

            override fun onSuccess(account: Account) {
                logcat("onSuccess")

                val number = account.phoneNumber
                val phoneNumber = number.toString()
                val countryCode = number.countryCode

                logcat("NUMBER: $number")
                logcat("countryCode: $countryCode")

                // Save user phone number and country code
                preferencesHelper.phoneNumber = phoneNumber
                preferencesHelper.countryCode = countryCode

                // Change login state
                preferencesHelper.isLoggedIn = true

                // Start next activity
                view.closeActivityAndSendResultBAck()
            }

            override fun onError(accountKitError: AccountKitError) {
                logcat("Account Kit onError: ${accountKitError.userFacingMessage}")
                logcat("Account Kit onError: $accountKitError")
            }
        })

        logcat("saveAccountInfo - done")
    }
}