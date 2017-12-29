package com.pedromassango.herenow.data.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by pedromassango on 12/29/17.
 */
@IgnoreExtraProperties
data class Contact(var phoneNumber: String,
                   var contactName: String,
                   var lastLocation: String = "N/A",
                   var permissions: HashMap<String, Boolean> = HashMap(),
                   var lat: Double,
                   var lng: Double) {

    // Empty constructor for Firebase
    constructor(): this("", "", "", HashMap<String, Boolean>(),0.0, 0.0 )

    fun getSimpleName(): String {
        return if (contactName.contains(" ")) {
            val c1 = contactName[0]
            val c2 = contactName[contactName.indexOf(" ") + 1]
            String.format("%s%s", c1, c2)
        } else {
            val c1 = contactName[0]
            c1.toString()
        }.toUpperCase()
    }
}