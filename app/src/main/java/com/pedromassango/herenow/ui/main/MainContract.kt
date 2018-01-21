package com.pedromassango.herenow.ui.main

/**
 * Created by pedromassango on 12/28/17.
 */
class MainContract {

    companion object {

        val LOGIN_REQUEST = 766
    }

    interface View{
        fun startSplashActivity()
        fun startLoginActivity()
        fun showMapFragment()
    }

    interface Presenter{
        fun checkAppState()

    }
}