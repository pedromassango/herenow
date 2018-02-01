package com.pedromassango.herenow.services

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.pedromassango.herenow.app.HereNow.Companion.logcat

/**
 * Created by Pedro Massango on 2/1/18.
 */
class MyFirebaseInstanceIdService : FirebaseInstanceIdService(){

    override fun onTokenRefresh() {
        super.onTokenRefresh()

        val refreshedToken = FirebaseInstanceId.getInstance().token
        logcat("FCM Token: $refreshedToken")

        //TODO: save device token

    }
}