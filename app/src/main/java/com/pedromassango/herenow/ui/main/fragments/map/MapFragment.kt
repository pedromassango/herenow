package com.pedromassango.herenow.ui.main.fragments.map

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.pedromassango.herenow.R
import com.pedromassango.herenow.app.HereNow.Companion.logcat
import com.pedromassango.herenow.data.RepositoryManager
import com.pedromassango.herenow.data.model.Contact
import com.pedromassango.herenow.data.preferences.PreferencesHelper
import com.pedromassango.herenow.extras.ActivityUtils
import com.pedromassango.herenow.ui.main.fragments.BaseMapFragment
import kotlinx.android.synthetic.main.fragment_maps.view.*

/**
 * Created by pedromassango on 12/28/17.
 *
 * Show the Map with friends location (If available)
 */
class MapFragment : BaseMapFragment(), MapContract.View {

    companion object {

        fun getInstance(): MapFragment {
            return MapFragment()
        }
    }

    // MVP
    lateinit var presenter: MapPresenter

    private lateinit var myLocationMarker: MarkerOptions
    // This device marker on Map
    private lateinit var myMarker: Marker

    // Cicle arround the user location
    var circle: Circle? = null

    private val friendsMarker: HashMap<String, Marker> = hashMapOf()

    // Location updates delay and distance
    private val distance = 20F

    // TO update marker position
    private var arleadySet = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logcat("MapFragment -> onCreate()")

        // Prepare user marker on map
        myLocationMarker = MarkerOptions()
        myLocationMarker.title(getString(R.string.you_are_here))
        myLocationMarker.flat(true)

        // Setup presenter
        val preferencesHelper = PreferencesHelper(context!!)

        presenter = MapPresenter(this,
                preferencesHelper,
                RepositoryManager.contactsRepository(preferencesHelper))
    }



    override fun showGetFriendsLocationError() {
        with(root) {
            //mMapView.visibility = View.GONE
            tv_map_info.visibility = View.VISIBLE
            tv_map_info.text = getString(R.string.get_friends_location_error)
            tv_map_info.setOnClickListener { presenter.showFriendsOnMap() }
        }
    }

    override fun showNoFriendsMessage(showDialog: Boolean) {

        // Show info in popup window from Broadcast Receiver
        ActivityUtils.showPopupMessage(activity!!, R.string.no_friend_to_show_title, closeOnClick = true)

        // Show the dialog only one time
        if (showDialog) {

            val builder = AlertDialog.Builder(context!!)
            builder.setCancelable(false)
            builder.setTitle(R.string.no_friend_to_show_title)
            builder.setMessage(R.string.no_friend_to_show_message)
            builder.setPositiveButton(R.string.str_ok, null)
            builder.create()
                    .show() // Show the dialog
        }
    }

    override fun showLoader() = super.loader()

    override fun removeLoader() = super.dismissLoader()

    override fun onMapReady(mMap: GoogleMap?) {
        super.onMapReady(mMap)
        logcat("MapActivity -> onMapReady")

        // start fetch friends location
        presenter.showFriendsOnMap()
    }

    override fun showFriendOnMap(contact: Contact) {

        when (friendsMarker.containsKey(contact.phoneNumber)) {
            true -> friendsMarker[contact.phoneNumber]!!.position = LatLng(contact.lat, contact.lng)
            false -> {

                val mo = MarkerOptions()
                        .title(contact.contactName).snippet(contact.phoneNumber)
                        .position(LatLng(contact.lat, contact.lng))
                        .flat(true)

                val m = map!!.addMarker(mo)
                friendsMarker.put(contact.phoneNumber, m)
            }
        }
    }

    // Location updates
    override fun onLocationChanged(location: Location?) {
        logcat("onLocationChanged")
        logcat("onLocationChanged: provider -> ${location?.provider}")

        val userCurrentPosition = LatLng(location!!.latitude, location.longitude)

        if (arleadySet == 0) {
            // set the marker at first time
            myLocationMarker.position(userCurrentPosition)
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
            myMarker = map!!.addMarker(myLocationMarker)
            arleadySet = 100

            // Draw a circle on Map
            circle = map!!.addCircle(CircleOptions()
                    .center(userCurrentPosition)
                    .fillColor(ResourcesCompat.getColor(resources, R.color.map_circle_fill, null))
                    .radius(500.toDouble())
                    .strokeWidth(0.toFloat()))

        } else {

            // Update the hold position to a recent position
            myMarker.position = userCurrentPosition
            //move the camera to where user position is with a zoom of 20
            map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(userCurrentPosition, 20F))

            // Just update map center location
            circle!!.center = userCurrentPosition
        }

        //Save user location
        presenter.onUserLocationChanged(location.latitude, location.longitude)
    }
}
