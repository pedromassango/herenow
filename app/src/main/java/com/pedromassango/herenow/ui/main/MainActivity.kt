package com.pedromassango.herenow.ui.main

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.WindowManager
import android.widget.PopupWindow
import com.pedromassango.herenow.R
import com.pedromassango.herenow.app.HereNow
import com.pedromassango.herenow.data.preferences.PreferencesHelper
import com.pedromassango.herenow.services.MyBroadcastReceiver
import com.pedromassango.herenow.ui.intro.IntroActivity
import com.pedromassango.herenow.ui.login.LoginActivity
import com.pedromassango.herenow.ui.main.fragments.contacts.ContactsFragment
import com.pedromassango.herenow.ui.main.fragments.map.MapFragment
import com.pedromassango.herenow.ui.main.fragments.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.popup_window.view.*

class MainActivity : AppCompatActivity(), MainContract.View,
        BottomNavigationView.OnNavigationItemSelectedListener, MyBroadcastReceiver.IConnectionListener {

    //MVP
    private lateinit var presenter: MainPresenter

    // Toolbar for popup window
    lateinit var mToolbar: Toolbar

    private lateinit var popup: PopupWindow

    private fun initializeViews() {

        // Set a toolbar as actionBar
        mToolbar = (toolbar as Toolbar)
        setSupportActionBar(mToolbar)

        // Changing tollbar title and subtitle text color
        mToolbar.setTitleTextColor(resources.getColor(R.color.white))
        mToolbar.setSubtitleTextColor(resources.getColor(R.color.white))

        // Setting up popup Window
        popup = PopupWindow(this)
        val viewPopup = layoutInflater.inflate(R.layout.popup_window, null)
        popup.setBackgroundDrawable(ColorDrawable(resources.getColor(android.R.color.transparent)))
        popup.contentView = viewPopup

        // Set POPUP content with and height
        popup.width = WindowManager.LayoutParams.MATCH_PARENT
        popup.height = WindowManager.LayoutParams.WRAP_CONTENT

        // set a listener in bootom navigation bar
        bottom_navigation.setOnNavigationItemSelectedListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup views
        initializeViews()

        presenter = MainPresenter(this, PreferencesHelper(this))
        presenter.checkAppState()

        // Listen when device is connected to  internet
        HereNow.setConnectionListener(this)
    }

    private fun showPopupAlert(@StringRes message: Int,
                               bgColor: PopupColor = PopupColor.DEFAULT,
                               closeOnClick: Boolean = false) {

        val backgroundgColor = when (bgColor) {
            PopupColor.RED -> resources.getColor(R.color.red)
            PopupColor.DEFAULT -> resources.getColor(R.color.gradient_bottom)
        }

        val tvInfo = with(popup.contentView) { popup_textview }
        tvInfo.text = getString(message)
        tvInfo.setBackgroundColor(backgroundgColor)

        // Close the popup window when it lose the Focus
        popup.isOutsideTouchable = closeOnClick
        popup.isTouchable = closeOnClick

        // show the popup window
        popup.showAsDropDown(mToolbar)
    }

    private fun dismissPopup() = if (popup.isShowing) popup.dismiss() else {
    }

    // Notified when a internet connection state as changed
    override fun onConnectionChanged(connected: Boolean) {
        when (connected) {
            true -> dismissPopup()
            false -> showPopupAlert(R.string.not_connection, bgColor = PopupColor.RED)
        }
    }

    override fun startSplashActivity() = startActivity(Intent(this, IntroActivity::class.java))
    override fun startLoginActivity() = startActivityForResult(Intent(this, LoginActivity::class.java), MainContract.LOGIN_REQUEST)

    override fun showMapFragment() {

        // Select the map fragment
        // in bottom navigation view
        bottom_navigation.selectedItemId = R.id.action_home
    }

    // BottomNavigationView item selected listener
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val title: String

        // Check the item clicked
        item.isChecked = true

        val fragment: Fragment = when (id) {

            R.id.action_home -> {
                title = getString(R.string.map)
                MapFragment.getInstance()
            }
            R.id.action_contacts -> {
                title = getString(R.string.contacts)
                ContactsFragment.getInstance()
            }
            R.id.action_settings -> {
                title = getString(R.string.settings)
                SettingsFragment.getInstance()
            }
            else -> {
                title = getString(R.string.map)
                MapFragment.getInstance()
            }
        }

        // Change activity title, with the selected fragment name
        this.title = title

        // Show the selected frament
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit()

        return false
    }
}
