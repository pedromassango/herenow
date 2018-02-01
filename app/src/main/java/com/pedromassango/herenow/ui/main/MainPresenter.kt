package com.pedromassango.herenow.ui.main

import com.pedromassango.herenow.data.preferences.PreferencesHelper
import com.pedromassango.herenow.extras.NotificationSender

/**
 * Created by pedromassango on 12/28/17.
 */
class MainPresenter(private val view: MainContract.View,
                    private val preferencesHelper: PreferencesHelper) : MainContract.Presenter {

    override fun checkAppState() {

        // If is first time, start intro activity
        if (preferencesHelper.isFirstTime) {
            view.startSplashActivity()
            return
        }

        // If is logged in, start maps and user location on map
        // Also, check state of autoLogin on settings
        val autoLogin = preferencesHelper.autoLogin
        val isLoggedIn = preferencesHelper.isLoggedIn

        // Check login status, only enter on application if it is logged in and Auto login is activated
        if (isLoggedIn && autoLogin) {

            // Subscribe the user on their number topic to receive notifications
            NotificationSender.subscribe(preferencesHelper.phoneNumber)
            // Show map fragment
            view.showMapFragment()

        } else {
            view.startLoginActivity()
        }
    }
}