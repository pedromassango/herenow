package com.pedromassango.herenow.ui.main

/**
 * Created by pedromassango on 12/28/17.
 */
class MainContract {

    interface View{
        fun startSplashActivity()
        fun startLoginActivity()
        fun showMapFragment()

    }

    interface Presenter{
        fun checkAppState()

    }
}