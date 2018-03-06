package com.pedromassango.herenow.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.pedromassango.herenow.R

/**
 * Created by Pedro Massango on 13/06/2017 at 21:52.
 */

class PreferencesHelper(context: Context) {

    // Keys to fetch
    private val KEY_AUTO_LOGIN = context.getString(R.string.prefs_auto_login)
    private val KEY_SHOW_NOTIFICATION = context.getString(R.string.prefs_notification)
    private val KEY_LOGGED_IN = "com.pedromassango.herenow.database.keys.KEY_LOGGED_IN"
    private val KEY_FIRST_TIME = "com.pedromassango.herenow.database.keys.KEY_FIRST_TIME"
    private val KEY_NO_FRIENDS_DIALOG_SHOWN = "com.pedromassango.herenow.database.keys.KEY_NO_FRIENDS_DIALOG_SHOWN"
    private val KEY_NUMBER = "com.pedromassango.herenow.database.keys.KEY_NUMBER"
    private val KEY_LOGIN_TOKEN = "com.pedromassango.herenow.database.keys.KEY_LOGIN_TOKEN"
    private val KEY_COUNTRY_CODE = "com.pedromassango.herenow.database.keys.KEY_C_CODE"
    private val KEY_FIRST_TIME_DATA_LOADED = "com.pedromassango.herenow.database.keys.KEY_FIRST_TIME_DATA_LOADED"

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = preferences.edit()

    var isFirstTime = preferences.getBoolean(KEY_FIRST_TIME, true)
        set(value) = editor.putBoolean(KEY_FIRST_TIME, value).apply()

    var dataLoaded = preferences.getBoolean(KEY_FIRST_TIME_DATA_LOADED, true)
        set(value) = editor.putBoolean(KEY_FIRST_TIME_DATA_LOADED, value).apply()

    var noFriendsDialogShown = preferences.getBoolean(KEY_NO_FRIENDS_DIALOG_SHOWN, false)
        set(value) = editor.putBoolean(KEY_NO_FRIENDS_DIALOG_SHOWN, value).apply()

    var isLoggedIn = preferences.getBoolean(KEY_LOGGED_IN, false)
        set(value) = editor.putBoolean(KEY_LOGGED_IN, value).apply()

    var autoLogin = preferences.getBoolean(KEY_AUTO_LOGIN, true)
        set(value) = editor.putBoolean(KEY_AUTO_LOGIN, value).apply()

    var showNotification = preferences.getBoolean(KEY_SHOW_NOTIFICATION, true)
        set(value) = editor.putBoolean(KEY_SHOW_NOTIFICATION, value).apply()

    /*var usename = preferences.getString(KEY_NAME, "")
        set(value) = editor.putString(KEY_NAME, value).apply()*/

    var token = preferences.getString(KEY_LOGIN_TOKEN, "")!!
        set(value) = editor.putString(KEY_LOGIN_TOKEN, value).apply()

    var phoneNumber = preferences.getString(KEY_NUMBER, "")!!
        set(value) = editor.putString(KEY_NUMBER, value).apply()

    var countryCode = preferences.getString(KEY_COUNTRY_CODE, "")!!
        set(value) = editor.putString(KEY_COUNTRY_CODE, value).apply()

    fun logout() {
        val editor = preferences.edit()
        editor.remove(KEY_COUNTRY_CODE)
        editor.remove(KEY_NUMBER)
        editor.remove(KEY_LOGGED_IN)
        editor.remove(KEY_AUTO_LOGIN)
        editor.apply()
    }

}
