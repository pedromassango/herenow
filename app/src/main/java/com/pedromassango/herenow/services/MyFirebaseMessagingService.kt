package com.pedromassango.herenow.services

import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pedromassango.herenow.app.HereNow.Companion.logcat
import com.pedromassango.herenow.extras.Constants

/**
 * Created by Pedro Massango on 2/1/18.
 */
class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage?) {
        //super.onMessageReceived(p0)
        val remoteMessage = p0!!


        // Log sender
        logcat("From: ${remoteMessage.from}")

        // Check if message contains a notification payload
        if(remoteMessage.notification != null){
            // Notification sent from FCM console
            logcat("Message notification body: ${remoteMessage.notification!!.body}")
        }

        //TODO: process custom notification
        val nData = remoteMessage.data
        logcat("nData: $nData")

        val nType = nData[Constants.NOTIFICATION_TYPE]
        logcat("nData: $nType")

        val nSenderNumber = nData[Constants.SENDER_NUMBER]
        logcat("nData: $nSenderNumber")

        Toast.makeText(applicationContext, "$nType :: $nSenderNumber", Toast.LENGTH_LONG).show()
        Toast.makeText(applicationContext, "$nType :: $nSenderNumber", Toast.LENGTH_LONG).show()
    }
}