package com.pedromassango.herenow.ui.login

import com.facebook.accountkit.Account
import com.facebook.accountkit.AccountKit
import com.facebook.accountkit.AccountKitCallback
import com.facebook.accountkit.AccountKitError
import com.pedromassango.herenow.app.HereNow.Companion.logcat
import com.pedromassango.herenow.data.preferences.PreferencesHelper
import com.pedromassango.herenow.extras.Utils

/**
 * Created by pedromassango on 12/28/17.
 */
class LoginPresenter(val view: LoginContract.View,
                     val preferencesHelper: PreferencesHelper) : LoginContract.Presenter {

    // Request login in AccountKit
    override fun startLoginRequest() {
        view.startAccountKitActivity()
    }

    override fun saveAccountInfo() {
        logcat("saveAccountInfo...")

        // show a loader
        view.showLoader()

        // AccoutKit to retrieve user information
        AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {

            override fun onSuccess(account: Account) {
                logcat("onSuccess")

                val number = account.phoneNumber
                var phoneNumber = number.toString()
                val countryCode = number.countryCode

                logcat("number: $number")
                logcat("RAWnumber: ${number.rawPhoneNumber}")
                logcat("countryCode: $countryCode")
                logcat("countryCodeISO: ${number.countryCodeIso}")

                logcat("saving upser data remotely")

                // Format phone number before save
                val formatedPhoneNumber = Utils.getFormatedNumber(phoneNumber)

                // Change login state
                preferencesHelper.isLoggedIn = true
                preferencesHelper.autoLogin = true

                // Save user phone number and country code
                preferencesHelper.phoneNumber = formatedPhoneNumber
                preferencesHelper.countryCode = countryCode

                // remove loader
                view.dismissLoader()

                // Start next activity
                view.closeActivity()
            }

            override fun onError(accountKitError: AccountKitError) {
                logcat("Account Kit onError: ${accountKitError.userFacingMessage}")
                logcat("Account Kit onError: $accountKitError")

                view.dismissLoader()
                view.showLoginErrorMessage()
            }
        })

        logcat("saveAccountInfo - done")
    }
}