package com.pedromassango.herenow.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.pedromassango.herenow.R
import com.pedromassango.herenow.ui.main.fragments.ContactsFragment
import com.pedromassango.herenow.ui.main.fragments.MapFragment
import com.pedromassango.herenow.ui.main.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private fun initializeViews() {

        // Set a toolbar as actionBar
        setSupportActionBar(toolbar as Toolbar)

        // set a listener in bootom navigation bar
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        bottom_navigation.selectedItemId = R.id.action_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup views
        initializeViews()

    }

    // BottomNavigationView item selected listener
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val title: String

        // Select the item clicked
        bottom_navigation.selectedItemId = id

        val fragment = when (id) {

            R.id.action_home -> {
                title = getString(R.string.map)
                MapFragment.getInstance()
            }
            R.id.action_contacts -> {
                title = getString(R.string.peoples)
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
        setTitle( title)

        // Show the selected frament
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit()

        return false
    }
}
