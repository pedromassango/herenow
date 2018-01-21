package com.pedromassango.herenow.ui.main.fragments.places

import com.pedromassango.herenow.data.model.Place

/**
 * Created by Pedro Massango on 1/18/18.
 *
 * Places click listener
 */
interface IPlaceClickListener{
    operator fun invoke(place: Place)
}