package com.pedromassango.herenow.data.preferences

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by Pedro Massango on 13/06/2017 at 21:52.
 */

class PreferencesHelper(context: Context) {

    // Keys to fetch
    private val KEY_LOGGED_IN = "com.pedromassango.herenow.database.keys.KEY_LOGGED_IN"
    private val KEY_FIRST_TIME = "com.pedromassango.herenow.database.keys.KEY_FIRST_TIME"
    private val KEY_NAME = "com.pedromassango.herenow.database.keys.KEY_NAME"
    private val KEY_NUMBER = "com.pedromassango.herenow.database.keys.KEY_NUMBER"
    private val KEY_COUNTRY_CODE = "com.pedromassango.herenow.database.keys.KEY_C_CODE"

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = preferences.edit()

    var isFirstTime = preferences.getBoolean(KEY_FIRST_TIME, true)
    set(value) = editor.putBoolean(KEY_FIRST_TIME, value).apply()

    var isLoggedIn = preferences.getBoolean(KEY_LOGGED_IN, false)
    set(value) = editor.putBoolean(KEY_LOGGED_IN, value).apply()

    var usename = preferences.getString(KEY_NAME, "")
        set(value) = editor.putString(KEY_NAME, value).apply()

    var phoneNumber = preferences.getString(KEY_NUMBER, "")
        set(value) = editor.putString(KEY_NUMBER, value).apply()

    var countryCode = preferences.getString(KEY_COUNTRY_CODE, "")
        set(value) = editor.putString(KEY_COUNTRY_CODE, value).apply()

    fun logout(context: Context) {
        val editor = preferences!!.edit()
        editor.remove(KEY_COUNTRY_CODE)
        editor.remove(KEY_NUMBER)
        editor.remove(KEY_NAME)
        editor.apply()
    }

}
