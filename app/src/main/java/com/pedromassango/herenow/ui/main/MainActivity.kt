package com.pedromassango.herenow.ui.main

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.pedromassango.herenow.R
import com.pedromassango.herenow.app.HereNow
import com.pedromassango.herenow.data.preferences.PreferencesHelper
import com.pedromassango.herenow.extras.ActivityUtils
import com.pedromassango.herenow.extras.Utils
import com.pedromassango.herenow.services.NetworkBroadcastReceiver
import com.pedromassango.herenow.services.CommonBroadcastReceiver
import com.pedromassango.herenow.ui.intro.IntroActivity
import com.pedromassango.herenow.ui.login.LoginActivity
import com.pedromassango.herenow.ui.main.fragments.contacts.ContactsFragment
import com.pedromassango.herenow.ui.main.fragments.map.MapFragment
import com.pedromassango.herenow.ui.main.fragments.places.FragmentShowPlacesOnMap
import com.pedromassango.herenow.ui.main.fragments.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_shet_content.*
import kotlinx.android.synthetic.main.popup_window.view.*

class MainActivity : AppCompatActivity(), MainContract.View,
        BottomNavigationView.OnNavigationItemSelectedListener, NetworkBroadcastReceiver.IConnectionListener, CommonBroadcastReceiver.IShowPopupListener {

    //MVP
    private lateinit var presenter: MainPresenter

    // Store current fragment id
    var currentFragmentId = 0

    // Toolbar for popup window
    private lateinit var mToolbar: Toolbar

    private lateinit var popup: PopupWindow
    // We should not show popupWindow in some fragments, this property will store the sow state
    private var canShowPopup = true
    // To handle it on Fragment selection
    var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null

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

        // SETUP bottom Sheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)

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
        // Check if the device have installed Google play Service, if not, close the app
        when(ActivityUtils.checkGooglePlayServices(this)){
            false -> this.finish()
        }
    }

    override fun showMapFragment() {

        // Select the map fragment
        // in bottom navigation view
        bottom_navigation.selectedItemId = R.id.action_home
    }

    /**
     * SHow a popup menu info bellow a Toolbar.
     */
    private fun showPopupAlert(@StringRes message: Int,
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

        if (canShowPopup) {
            // show the popup window
            popup.showAsDropDown(mToolbar)
        }
    }

    /**
     * Dismiss the popup bellow a toolbar if it is shown.
     */
    private fun dismissPopup() = if (popup.isShowing) popup.dismiss() else {
    }

    /**
     * Show popup message requested by CommonBroadcastReceiver
     */
    override fun onBroadcastShowPopup(@StringRes message: Int, closeOnClick: Boolean) {

        // If we're not connected, do not show any other message
        if (!Utils.isConnected(this)) {
            return
        }

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

        // Check if the selected fragment is not arleady shown
        if (currentFragmentId == id) {
            return true
        }


        var title = getString(R.string.app_name)

        // Remove popup window, if it is shown
        dismissPopup()

        val fragment: Fragment = when (id) {

            R.id.action_home -> MapFragment.getInstance()
            R.id.action_places -> {
                // show or hide bottomShet, on locations item click
                when (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
                    true -> bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
                    else -> bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
                }
                null
            }
            R.id.action_contacts -> {
                title = getString(R.string.contacts)
                ContactsFragment.getInstance()
            }
            R.id.action_settings -> {
                // Disable show popup window on settings fragment
                canShowPopup = false

                title = getString(R.string.settings)
                SettingsFragment.getInstance()
            }
            else -> MapFragment.getInstance()
        } ?: return true

        // On Navigation item click, close the bottomSheet
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED

        // Change activity title, with the selected fragment name
        this.title = title

        // Replace the fragments
        ActivityUtils.replaceFragment(supportFragmentManager, fragment = fragment)

        // Save current item id
        currentFragmentId = id

        return true
    }

    override fun onBackPressed() {
        // Check if map places fragment is visible if it is visible, then
        // replace with fragment list places.
        when (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
            true -> bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_COLLAPSED
            false -> super.onBackPressed()
        }
    }
}
