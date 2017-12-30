package com.pedromassango.herenow.ui.main

import com.pedromassango.herenow.data.preferences.PreferencesHelper

/**
 * Created by pedromassango on 12/28/17.
 */
class MainPresenter(private val view: MainContract.View,
                    private val preferencesHelper: PreferencesHelper) : MainContract.Presenter {

    override fun checkAppState() {

        // If is first time, start intro activity
        if(preferencesHelper.isFirstTime){
            view.startSplashActivity()
            return
        }

        // If is logged in, start maps and user location on map
        if(preferencesHelper.isLoggedIn){
            view.showMapFragment()
        }else{
            view.startLoginActivity()
        }
    }
}