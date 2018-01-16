package com.pedromassango.herenow.ui.main

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.WindowManager
import android.widget.PopupWindow
import com.pedromassango.herenow.R
import com.pedromassango.herenow.app.HereNow
import com.pedromassango.herenow.data.preferences.PreferencesHelper
import com.pedromassango.herenow.services.NetworkBroadcastReceiver
import com.pedromassango.herenow.services.PopupBroadcastReceiver
import com.pedromassango.herenow.ui.intro.IntroActivity
import com.pedromassango.herenow.ui.login.LoginActivity
import com.pedromassango.herenow.ui.main.fragments.contacts.ContactsFragment
import com.pedromassango.herenow.ui.main.fragments.map.MapFragment
import com.pedromassango.herenow.ui.main.fragments.places.FragmentPlaces
import com.pedromassango.herenow.ui.main.fragments.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.popup_window.view.*

class MainActivity : AppCompatActivity(), MainContract.View,
        BottomNavigationView.OnNavigationItemSelectedListener, NetworkBroadcastReceiver.IConnectionListener, PopupBroadcastReceiver.IShowPopupListener {

    //MVP
    private lateinit var presenter: MainPresenter

    // Toolbar for popup window
    private lateinit var mToolbar: Toolbar

    private lateinit var popup: PopupWindow

    private fun initializeViews() {

        // Set a toolbar as actionBar
        mToolbar = (toolbar as Toolbar)
        setSupportActionBar(mToolbar)

        // Setting up popup Window
        popup = PopupWindow(this)
        val viewPopup = layoutInflater.inflate(R.layout.popup_window, null)
        popup.setBackgroundDrawable(ColorDrawable(ResourcesCompat.getColor(resources, android.R.color.transparent, null)))
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

        // Setup activity views
        initializeViews()

        presenter = MainPresenter(this, PreferencesHelper(this))
        presenter.checkAppState()

        // Listen when device is connected to  internet
        HereNow.setConnectionListener(this)

        // Listen for popup message request
        HereNow.setPopupListener(this)
    }

    override fun onResume() {
        super.onResume()
        // Check again when comming from LoginActivity
        //presenter.checkAppState()
    }

    override fun showMapFragment() {

        // Select the map fragment
        // in bottom navigation view
        bottom_navigation.selectedItemId = R.id.action_home
    }

    /**
     * SHow a popup menu info bellow a Toolbar.
     */
    fun showPopupAlert(@StringRes message: Int,
                       bgColor: PopupColor = PopupColor.DEFAULT,
                       closeOnClick: Boolean = true) {

        val backgroundgColor = when (bgColor) {
            PopupColor.RED -> ResourcesCompat.getColor(resources, R.color.red, null)
            PopupColor.DEFAULT -> ResourcesCompat.getColor(resources, R.color.gradient_bottom, null)
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

    /**
     * Dismiss the popup bellow a toolbar if it is shown.
     */
    private fun dismissPopup() = if (popup.isShowing) popup.dismiss() else { }

    /**
     * Show popup message requested by PopupBroadcastReceiver
     */
    override fun onBroadcastShowPopup(@StringRes message: Int, closeOnClick: Boolean) {
        showPopupAlert(message = message, closeOnClick = closeOnClick)
    }

    // Notified when a internet connection state as changed
    override fun onConnectionChanged(connected: Boolean) {
        when (connected) {
            true -> dismissPopup()
            false -> showPopupAlert(R.string.not_connection, bgColor = PopupColor.RED, closeOnClick = false)
        }
    }

    override fun startSplashActivity() = startActivity(Intent(this, IntroActivity::class.java))
    override fun startLoginActivity() = startActivityForResult(Intent(this, LoginActivity::class.java), MainContract.LOGIN_REQUEST)

    // BottomNavigationView item selected listener
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        var title = getString(R.string.app_name)

        // Check the item clicked
        item.isChecked = true

        // Remove popup window, if it is shown
        dismissPopup()

        val fragment: Fragment = when (id) {

            R.id.action_home -> MapFragment.getInstance()
            R.id.action_places ->{
                title = getString(R.string.neaby_places)
                FragmentPlaces.getInstance()
            }
            R.id.action_contacts -> {
                title = getString(R.string.contacts)
                ContactsFragment.getInstance()
            }
            R.id.action_settings -> {
                title = getString(R.string.settings)
                SettingsFragment.getInstance()
            }
            else -> MapFragment.getInstance()
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
