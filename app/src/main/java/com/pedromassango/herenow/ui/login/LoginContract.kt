package com.pedromassango.herenow.ui.login

/**
 * Created by pedromassango on 12/28/17.
 */
class LoginContract {

    companion object {

        const val ACCOUNT_KIT_LOGIN_REQUEST_CODE = 1024
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