package com.pedromassango.herenow.ui.main.fragments.settings

import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pedromassango.herenow.R

/**
 * Created by pedromassango on 12/28/17.
 */
class SettingsFragment : PreferenceFragment() {

    companion object {
        private var INSTANCE: SettingsFragment? = null

        fun getInstance(): SettingsFragment {
            if(INSTANCE == null){
                INSTANCE = SettingsFragment()
            }
            return INSTANCE!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings_fragment)
    }
}