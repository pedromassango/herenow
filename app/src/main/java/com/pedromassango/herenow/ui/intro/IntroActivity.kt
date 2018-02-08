package com.pedromassango.herenow.ui.intro

import android.Manifest
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage
import com.pedromassango.herenow.R
import com.pedromassango.herenow.data.preferences.PreferencesHelper
import com.pedromassango.herenow.ui.main.MainActivity

/**
 * Created by pedromassango on 12/28/17.
 */
class IntroActivity : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        //addSlide(ProgrammersSlide.newInstance(R.layout.app_intro3));

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.

        // Sliders background color
        val drawableColor = ColorDrawable( ResourcesCompat.getColor(resources, R.color.gradient_bottom, null))
        val color = drawableColor.color

        val sp = SliderPage()
        sp.title = getString(R.string.intro_one_title)
        sp.description = getString(R.string.intro_one_description)
        sp.imageDrawable = R.mipmap.ic_launcher
        sp.bgColor = color

        // Intro 1
        addSlide(AppIntroFragment.newInstance(sp))

        sp.title = getString(R.string.intro_two_title)
        sp.description = getString(R.string.intro_two_description)
        sp.imageDrawable = R.mipmap.ic_launcher
        sp.bgColor = color

        // Intro 2
        addSlide(AppIntroFragment.newInstance(sp))

        sp.title = getString(R.string.intro_tree_title)
        sp.description = getString(R.string.intro_tree_description)
        sp.imageDrawable = R.drawable.ic_friends
        sp.bgColor = color

        // Request permissions on fragment 3 and 4
        askForPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 3)
        askForPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 4)

        // Intro 3
        addSlide(AppIntroFragment.newInstance(sp))

        sp.title = getString(R.string.intro_four_title)
        sp.description = getString(R.string.intro_four_description)
        sp.imageDrawable = R.mipmap.ic_launcher
        sp.bgColor = color

        // Intro 4
        addSlide(AppIntroFragment.newInstance(sp))

        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(false)
        isProgressButtonEnabled = true

        // Enable fade animation
        setFadeAnimation()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)

        // Change first time state
        PreferencesHelper(this.applicationContext)
                .isFirstTime = false

        // Start Login Activity
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}