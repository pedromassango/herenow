package com.pedromassango.herenow.data.model

/**
 * Created by Pedro Massango on 1/17/18.
 */
data class Place(var placeName: String,
                 var placeType: String = "",
                 var vicinity: String = "",
                 var lat: Double = 0.0,
                 var lng: Double = 0.0)