package com.pedromassango.herenow.ui.main.fragments.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
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
class MapFragment : BaseMapFragment(), MapContract.View, LocationListener {

    companion object {

        fun getInstance(): MapFragment {
            return MapFragment()
        }
    }

    // MVP
    lateinit var presenter: MapPresenter

    private lateinit var locationManager: LocationManager
    private lateinit var myLocationMarker: MarkerOptions
    private lateinit var myMarker: Marker

    private val friendsMarker: HashMap<String, Marker> = hashMapOf()

    // Location updates delay and distance
    private val distance = 20F
    private val timeUpdate = 5000L // 5sec

    // TO update marker position
    private var arleadySet = 0

    // View
    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup locationManager
        locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Prepare user marker on map
        myLocationMarker = MarkerOptions()

        myLocationMarker.title(getString(R.string.you_are_here))
        //myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
        myLocationMarker.flat(true)

        // Setup presenter
        val preferencesHelper = PreferencesHelper(context)

        presenter = MapPresenter(this,
                preferencesHelper,
                RepositoryManager.contactsRepository(preferencesHelper))
    }

    override fun showGetFriendsLocationError() {
        with(root) {
            mMapView.visibility = View.GONE
            tv_map_info.visibility = View.VISIBLE
            tv_map_info.text = getString(R.string.get_friends_location_error)
            tv_map_info.setOnClickListener { presenter.showFriendsOnMap() }
        }
    }

    override fun showNoFriendsMessage(showDialog: Boolean) {

        // Show info in popup window from Broadcast Receiver
        ActivityUtils.showPopupMessage(activity, R.string.no_friend_to_show_title, closeOnClick = false)

        // Show the dialog only one time
        if (showDialog) {

            val builder = AlertDialog.Builder(context)
            builder.setCancelable(false)
            builder.setTitle(R.string.no_friend_to_show_title)
            builder.setMessage(R.string.no_friend_to_show_message)
            builder.setPositiveButton(R.string.str_ok, null)
            builder.create()
                    .show() // Show the dialog
        }
    }

    override fun showLoader() = super.Loader()

    override fun removeLoader() = super.dismissLoader()

    @SuppressLint("MissingPermission")
    override fun onMapReady(mMap: GoogleMap?) {
        this.map = mMap

        // start fetch friends location
        presenter.showFriendsOnMap()

        // Request location updates via GPS
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeUpdate, distance, this)
        // Request location updates via PASSIVE-PROVIDER
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, timeUpdate, distance, this)
        // Request location updates via NETWORK
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, timeUpdate, distance, this)
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

        if (arleadySet == 0) {
            // set the marker at first time
            myLocationMarker.position(LatLng(location!!.latitude, location.longitude))
            myMarker = map!!.addMarker(myLocationMarker)
            arleadySet = 100
        } else {
            // Update the hold position to a recent position
            myMarker.position = LatLng(location!!.latitude, location.longitude)
        }

        //Save user location
        presenter.onUserLocationChanged(location.latitude, location.longitude)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) = logcat("onStatusChanged:  $provider")

    override fun onProviderDisabled(provider: String?) = logcat("onProviderDisabled:  $provider")

    override fun onProviderEnabled(provider: String?) = logcat("onProviderEnabled:  $provider")
}
