package com.pedromassango.herenow.app

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.pedromassango.herenow.services.CommonBroadcastReceiver
import com.pedromassango.herenow.services.NetworkBroadcastReceiver

/**
 * Created by pedromassango on 12/28/17.
 */
class HereNow : Application() {

    override fun onCreate() {
        super.onCreate()

        // Setup firebase
        FirebaseApp.initializeApp(this)

        // Enable firebase to persist data when offline
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    companion object {

        // Register a connection listner to a broadcast
        fun setConnectionListener(iConnectionListener: NetworkBroadcastReceiver.IConnectionListener){
            checkNotNull(iConnectionListener)
            NetworkBroadcastReceiver.iConnectionListener = iConnectionListener
        }

        // Register popup listener to a broadcast
        fun setPopupListener(iShowCommonListener: CommonBroadcastReceiver.IShowPopupListener){
            checkNotNull(iShowCommonListener)
            CommonBroadcastReceiver.iShowPopupListener = iShowCommonListener
        }

        fun logcat(message: String){
            Log.v("output", "OUT: $message")

            println("OUT: $message")
        }

    }
}