package com.pedromassango.herenow.data.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by pedromassango on 12/29/17.
 */
@IgnoreExtraProperties
data class Contact(var phoneNumber: String,
                   var contactName: String = "N/A",
                   var lastLocation: String = "N/A",
                   var permissions: HashMap<String, Boolean> = HashMap(),
                   var lat: Double = 0.0,
                   var lng: Double = 0.0) {

    // Empty constructor for Firebase
    constructor(): this("")

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

    fun toMap(): Map<String, Any> {
        val map = java.util.HashMap<String, Any>()
        map.put(phoneNumber, contactName)
        return map
    }
}