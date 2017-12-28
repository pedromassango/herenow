package com.pedromassango.herenow.extras

import android.content.Context
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator

/**
 * Created by Pedro Massango on 26/05/2017.
 */

object AnimUtils {

    fun blink(): Animation {

        val animation = AlphaAnimation(1f, 0f)
        animation.duration = 550
        animation.interpolator = LinearInterpolator()
        animation.repeatCount = 10
        animation.repeatMode = Animation.REVERSE
        return animation
    }

    fun transOut(context: Context): Animation {

        return AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
    }

    fun transIn(context: Context): Animation {

        return AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
    }

}
