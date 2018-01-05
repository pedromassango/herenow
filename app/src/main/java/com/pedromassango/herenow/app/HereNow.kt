package com.pedromassango.herenow.app

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.pedromassango.herenow.services.MyBroadcastReceiver

/**
 * Created by pedromassango on 12/28/17.
 */
class HereNow : Application() {

    override fun onCreate() {
        super.onCreate()

        // Setup firebase
        FirebaseApp.initializeApp(this);

        // Enable firebase to persist data when offline
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    companion object {

        fun setConnectionListener(iConnectionListener: MyBroadcastReceiver.IConnectionListener){
            checkNotNull(iConnectionListener)
            MyBroadcastReceiver.iConnectionListener = iConnectionListener
        }

        fun logcat(message: String){
            Log.v("output", "OUT: $message")
        }

    }
}