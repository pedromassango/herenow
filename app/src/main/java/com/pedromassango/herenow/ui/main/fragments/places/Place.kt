package com.pedromassango.herenow.ui.main.fragments.places

import android.support.annotation.DrawableRes

/**
 * Created by Pedro Massango on 1/17/18.
 */
data class Place(var placeName: String,
                 @DrawableRes var placeIcon: Int,
                 var placeId: Int = 0)