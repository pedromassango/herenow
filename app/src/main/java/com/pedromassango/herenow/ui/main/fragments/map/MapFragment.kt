package com.pedromassango.herenow.ui.main.fragments.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.pedromassango.herenow.R
import com.pedromassango.herenow.app.HereNow.Companion.logcat
import com.pedromassango.herenow.data.RepositoryManager
import com.pedromassango.herenow.data.model.Contact
import com.pedromassango.herenow.data.preferences.PreferencesHelper
import kotlinx.android.synthetic.main.fragment_maps.view.*

/**
 * Created by pedromassango on 12/28/17.
 */
class MapFragment : Fragment(), MapContract.View, OnMapReadyCallback, LocationListener {

    companion object {

        fun getInstance(): MapFragment {
            return MapFragment()
        }
    }

    // MVP
    lateinit var presenter: MapPresenter

    //Map
    var map: GoogleMap? = null
    private lateinit var locationManager: LocationManager
    private lateinit var myLocationMarker: MarkerOptions
    private lateinit var myMarker: Marker

    private val friendsMarker: HashMap<String, Marker> = hashMapOf()

    // Location updates delay and distance
    private val distance = 0F
    private val timeUpdate = 1000L // 1sec

    // TO update marker position
    private var arleadySet = false

    // View
    lateinit var root: View

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
        presenter = MapPresenter(this,
                RepositoryManager.contactsRepository(PreferencesHelper(context)))
    }

    override fun onDestroy() {
        map = null
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater!!.inflate(R.layout.fragment_maps, container, false)
        logcat("onCreateView -> MapFragment")

        // Setting up MapFragment
        //val mapFragment = fragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        //mapFragment.getMapAsync(this)
        //getMapAsync(this)

        return root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: enable it to fetch friends location
        removeLoader()
        //presenter.showFriendsOnMap()
    }

    override fun showGetFriendsLocationError() {
        with(root) {
            tv_map_info.visibility = View.VISIBLE
            tv_map_info.text = getString(R.string.get_friends_location_error)
            tv_map_info.setOnClickListener { presenter.showFriendsOnMap() }
        }
    }

    override fun showNoFriendsMessage() {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle(R.string.no_friend_to_show_title)
        builder.setMessage(R.string.no_friend_to_show_message)
        builder.setPositiveButton(R.string.str_ok, null)

        builder.create()
                .show()
    }

    override fun showLoader() {
        with(root) {
            progressbar_maps.visibility = View.VISIBLE
            tv_map_info.visibility = View.VISIBLE
            tv_map_info.text = getString(R.string.please_wait)
        }
    }

    override fun removeLoader() {
        with(root) {
            progressbar_maps.visibility = View.GONE
            tv_map_info.visibility = View.GONE
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(mMap: GoogleMap?) {
        logcat("onMapReady")

        this.map = mMap!!

        // Request location updates

        // Request location updates via NETWORK
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, timeUpdate, distance, this)
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeUpdate,distance, this)
    }

    override fun showFriendOnMap(contact: Contact) {

        when (friendsMarker.containsKey(contact.phoneNumber)) {
            true -> friendsMarker[contact.phoneNumber]!!.position = LatLng(contact.lat, contact.lng)
            false -> {

                val mo = MarkerOptions()
                        .title(contact.contactName).snippet(contact.phoneNumber)
                        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
                        .flat(true)

                val m = map!!.addMarker(mo)
                friendsMarker.put(contact.phoneNumber, m)
            }
        }
    }

    // Location updates
    override fun onLocationChanged(location: Location?) {
        logcat("onLocationChanged")

        if (arleadySet) {
            // set the marker at first time
            myLocationMarker.position(LatLng(location!!.latitude, location.longitude))
            myMarker = map!!.addMarker(myLocationMarker)
            arleadySet = true
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
