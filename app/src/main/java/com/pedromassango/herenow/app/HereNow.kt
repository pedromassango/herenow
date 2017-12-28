package com.pedromassango.herenow.app

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

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

        fun logcat(message: String){
            Log.v("output", "OUT: $message")
        }
    }
}