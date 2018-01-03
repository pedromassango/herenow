package com.pedromassango.herenow.ui.login

/**
 * Created by pedromassango on 12/28/17.
 */
class LoginContract {

    companion object {

        val ACCOUNT_KIT_LOGIN_REQUEST_CODE = 1024
        val ACTIVITY_LOGIN_RESULT_KEY = "com.pedromassango.herenow.ui.login.ACTIVITY_LOGIN_RESULT_KEY"
    }

    interface Model {

    }

    interface View {

        fun startAccountKitActivity()

        fun closeActivity()
        fun showLoginErrorMessage()
        fun showLoader()
        fun dismissLoader()
    }

    interface Presenter {
        fun startLoginRequest()
        fun saveAccountInfo()

    }

}