package com.pedromassango.herenow.ui.main.fragments.map

import android.location.Location
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

    // The app will only show a friend on map, if it have a minimun of five metters of distance
    private val MIN_DISTANCE_TO_SHOW_FRIEND_ON_MAP = 5
    // minimum distance to show a route betwen friend and the user
    private val MIN_DISTANCE_TO_SHOW_ROUTE = 50
    // maximum distance to show a route betwen friend and the user
    private val MAX_DISTANCE_TO_SHOW_ROUTE = 1000
    // Map circle radius in meters
    private val MAP_CIRCLE_RADIUS = 200.toDouble()

    // MVP
    private lateinit var presenter: MapPresenter

    // User location Marker
    private lateinit var myLocationMarker: MarkerOptions
    // User current position
    private var userCurrentPosition = LatLng(0.toDouble(), 0.0)

    // This device marker on Map
    private lateinit var myMarker: Marker

    // Cicle arround the user location
    private var circle: Circle? = null
    // Store users marker position, to just update instead of create it again on Map
    private val friendsMarkers: HashMap<String, Marker> = hashMapOf()

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

        // If distance betwen is less than 5 meters, hide friend on MAP
        logcat("DIST: ${userCurrentPosition.latitude}")
        if (userCurrentPosition.latitude != 0.0) {
            val distance = getDistance(userCurrentPosition, LatLng(contact.lat, contact.lng))
            if (distance < MIN_DISTANCE_TO_SHOW_FRIEND_ON_MAP) {
                return
            }
        }

        when (friendsMarkers.containsKey(contact.phoneNumber)) {
            true -> friendsMarkers[contact.phoneNumber]!!.position = LatLng(contact.lat, contact.lng)
            false -> {

                val mo = MarkerOptions()
                        .title(contact.contactName).snippet(contact.phoneNumber)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_friend))
                        .position(LatLng(contact.lat, contact.lng))
                        .flat(true)

                val marker = map!!.addMarker(mo)
                friendsMarkers[contact.phoneNumber] = marker
            }
        }
    }

    // Location updates
    override fun onLocationChanged(location: Location?) {
        logcat("onLocationChanged")
        logcat("onLocationChanged: provider -> ${location?.provider}")

        userCurrentPosition = LatLng(location!!.latitude, location.longitude)

        // Check if we need to create a marker and circle, or just update the position
        if (arleadySet == 0) {
            // set the marker at first time
            myLocationMarker.position(userCurrentPosition)
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_user))
            myMarker = map!!.addMarker(myLocationMarker)
            arleadySet = 100

            // Draw a circle on Map, with he user at center
            circle = map!!.addCircle(CircleOptions()
                    .center(userCurrentPosition)
                    .fillColor(ResourcesCompat.getColor(resources, R.color.map_circle_fill, null))
                    .radius(MAP_CIRCLE_RADIUS)
                    .strokeWidth(0.toFloat()))

        } else {

            // Update the hold position to a recent position
            myMarker.position = userCurrentPosition
            //move the camera to where user position is with a zoom of 20
            map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(userCurrentPosition, 15F))

            // Just update map center location
            circle!!.center = userCurrentPosition
        }

        // Trace a rout from user to their friend, if it is in bounds
        traceRouterBetwenFirends(userCurrentPosition)

        //Save user location
        presenter.onUserLocationChanged(location.latitude, location.longitude)
    }

    override fun getDistance(userLatLng: LatLng, friendLatLng: LatLng): Float {
        val holdResult = FloatArray(1)
        Location.distanceBetween(userLatLng.latitude, userLatLng.longitude,
                friendLatLng.latitude, friendLatLng.longitude, holdResult)
        return holdResult[0]
    }

    private fun traceRouterBetwenFirends(userCurrentPosition: LatLng) {

        // If have friends on list, trace a route
        if (friendsMarkers.size > 0) {
            friendsMarkers.forEach { marker ->
                // Calculate the distance betwen user and current friend on list
                logcat("Getting distance betwen friends...")


                // The distance
                val distanceBetwen = getDistance(userCurrentPosition, marker.value.position)
                logcat("Distance is: $distanceBetwen")

                // Only trace a route if the distance is betwen 50 to 1000 meters
                if (distanceBetwen.toInt() in MIN_DISTANCE_TO_SHOW_ROUTE..MAX_DISTANCE_TO_SHOW_ROUTE) {
                    logcat("Distance is greatter")

                    logcat("Showing route in Map")

                    //val route = map!!.addPolyline(PolylineOptions()
                    map!!.addPolyline(PolylineOptions()
                            .color(ResourcesCompat.getColor(resources, R.color.gradient_bottom, null))
                            .add(marker.value.position)
                            .add(userCurrentPosition)
                            .width(4F))
                }
            }

        }
    }
}
