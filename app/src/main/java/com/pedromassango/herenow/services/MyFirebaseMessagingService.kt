package com.pedromassango.herenow.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pedromassango.herenow.R
import com.pedromassango.herenow.app.HereNow.Companion.logcat
import com.pedromassango.herenow.extras.ActivityUtils
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

        val nType = nData[Constants.NOTIFICATION_TYPE]!!
        logcat("nData - nType: $nType")

        val nSenderNumber = nData[Constants.SENDER_NUMBER]!!
        logcat("nData - nSenderNumber: $nSenderNumber")

        var title = nSenderNumber
        var description = nType

        when(nType.toInt()){
           Constants.NOTIFICATION_TYPE_ADDED_AS_FRIEND->{
               val theNumber = getString(R.string.the_number)
               title = String.format("%s %s %s",theNumber, nSenderNumber, getString(R.string.added_you))

               description = String.format("%s %s %s", theNumber,
                       nSenderNumber, getString(R.string.whant_to_see_your_location))
           }
        }

        // Show the notification
        ActivityUtils.showNotification(applicationContext,
                title, description)
    }
}