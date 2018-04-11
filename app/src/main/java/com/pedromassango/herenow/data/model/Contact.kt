package com.pedromassango.herenow.data.model

import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by pedromassango on 12/29/17.
 */
@IgnoreExtraProperties
data class Contact(var phoneNumber: String,
                   var contactName: String = "N/A",
                   var lastLocation: String = "",
                   var allow: Boolean = false,
                   var lat: Double = 0.0,
                   var lng: Double = 0.0) {

    // Empty constructor for Firebase
    constructor(): this("")

    fun getSimpleName(): String {
        return if(contactName.trim().isEmpty()){
            phoneNumber
        }
        else if (contactName.contains(" ")) {
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
        map["phoneNumber"] = phoneNumber
        map["contactName"] = contactName
        map["lastLocation"] = lastLocation
        map["allow"] = allow
        map["lat"] = lat
        map["lng"] = lng
        return map
    }

    fun toDataMap(): Map<String, Any> {
        val map = java.util.HashMap<String, Any>()
        map["phoneNumber"] = phoneNumber
        map["contactName"] = contactName
        map["allow"] = allow
        return map
    }
}