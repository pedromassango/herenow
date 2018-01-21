package com.pedromassango.herenow.ui.main.fragments.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.preference.PreferenceCategory
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pedromassango.herenow.BuildConfig
import com.pedromassango.herenow.R
import com.pedromassango.herenow.data.preferences.PreferencesHelper
import com.pedromassango.herenow.extras.ActivityUtils
import com.pedromassango.herenow.ui.login.LoginActivity

/**
 * Created by pedromassango on 12/28/17.
 */
class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        //private var INSTANCE: SettingsFragment? = null

        fun getInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)

        // Changing Settings bacground color to transparent
        root?.setBackgroundColor(ResourcesCompat.getColor(resources, android.R.color.transparent, null))
        return root
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_fragment, rootKey)

        // Set the user phone number on preferences
        val prefssUsername = findPreference( getString(R.string.prefs_user_phone_number))
        prefssUsername.summary = PreferencesHelper(context!!).phoneNumber

        // Set the app version on version info preferences
        val prefsVersion = findPreference( getString(R.string.prefs_app_version))
        prefsVersion.summary = BuildConfig.VERSION_NAME

        // Logout click listener
        val prefsLogout = findPreference(getString(R.string.prefs_logout))
        prefsLogout.setOnPreferenceClickListener {

            // Remove user data
           PreferencesHelper(context!!).logout()
            // start login activity
            startActivity( Intent(context!!, LoginActivity::class.java))
            activity!!.finish()

            true
        }

        // Privacy Policy click listener
        val prefsPrivacyPolicy = findPreference(getString(R.string.prefs_privacy_policy_key))
        prefsPrivacyPolicy.setOnPreferenceClickListener {
            // Start a web browser to see privacy policy
            val privacyPolicyUrl = getString(R.string.privacy_policy_url)
            val privacyPolicyWebIntent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl))

            startActivity(privacyPolicyWebIntent)

            return@setOnPreferenceClickListener true
        }
    }
}