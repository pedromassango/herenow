package com.pedromassango.herenow.data.model

/**
 * Created by Pedro Massango on 4/11/18.
 */
class Notification(var authorNumber: String,
                   var authorName: String = "",
                   var allowed: Boolean = false,
                   var toNumber: String = "",
                   var id: String = ""){

    // For Firebase
    constructor(): this("")

    fun getNameOrNumber(): String{
        return if(authorName.trim().isEmpty()) authorNumber else authorName
    }

    fun toDataMap(): Map<String, Any> {
        val map = java.util.HashMap<String, Any>()
        map["authorName"] = authorName
        map["authorNumber"] = authorNumber
        map["allowed"] = allowed
        map["toNumber"] = toNumber
        map["id"] = id
        return map
    }
}