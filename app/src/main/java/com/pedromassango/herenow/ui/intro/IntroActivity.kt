package com.pedromassango.herenow.ui.intro

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.pedromassango.herenow.R
import com.pedromassango.herenow.data.PreferencesHelper
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
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.intro_one_title),
                getString(R.string.intro_one_description),
                R.mipmap.ic_launcher, resources.getColor(R.color.gradient_bottom)))

        addSlide(AppIntroFragment.newInstance(
                getString(R.string.intro_two_title),
                getString(R.string.intro_two_description),
                R.mipmap.ic_launcher, resources.getColor(R.color.gradient_bottom)))

        // Request permissions on fragment 3 and 4
        askForPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 3)
        askForPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 4)

        addSlide(AppIntroFragment.newInstance(
                getString(R.string.intro_tree_title),
                getString(R.string.intro_tree_description),
                R.drawable.ic_friends, resources.getColor(R.color.gradient_top)))

        addSlide(AppIntroFragment.newInstance(
                getString(R.string.intro_four_title),
                getString(R.string.intro_four_description),
                R.mipmap.ic_launcher, resources.getColor(R.color.gradient_top)))

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
        PreferencesHelper(this)
                .isFirstTime = false

        // Start Login Activity
        startActivity( Intent(this, MainActivity::class.java))
    }


}