package com.pedromassango.herenow.ui.main.fragments.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pedromassango.herenow.R

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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        root?.setBackgroundColor(resources.getColor(android.R.color.transparent))
        return root
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_fragment, rootKey)

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