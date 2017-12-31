package com.pedromassango.herenow.ui.main.fragments.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.pedromassango.herenow.R
import com.pedromassango.herenow.app.HereNow.Companion.logcat
import com.pedromassango.herenow.extras.Utils

/**
 * Created by pedromassango on 12/28/17.
 */
class MapFragment : Fragment(), MapContract.View, OnMapReadyCallback, LocationListener {

    companion object {
        private var INSTANCE: MapFragment? = null

        fun getInstance(): MapFragment {
            if (INSTANCE == null) {
                INSTANCE = MapFragment()
            }
            return INSTANCE!!
        }
    }

    // MVP
    lateinit var presenter: MapPresenter

    //Map
    var map: GoogleMap? = null
    private lateinit var locationManager: LocationManager
    private lateinit var myLocationMarker: MarkerOptions
    private lateinit var myMarker: Marker

    // TO update marker position
    private var arleadySet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup locationManager
        locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Prepare user marker on map
        myLocationMarker = MarkerOptions()

        myLocationMarker.title(getString(R.string.you_are_here))
        //myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on))
        myLocationMarker.draggable(false)
        myLocationMarker.flat(true)

        // Setup presenter
        presenter = MapPresenter(this)
    }

    override fun onDestroy() {
        map = null
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_maps, container, false)

        // Setting up MapFragment
        //val mapFragment = view.findViewById<View>(R.id.map) as SupportMapFragment
        //mapFragment.getMapAsync(this)

        return view
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(mMap: GoogleMap?) {
        logcat("onMapReady")
        this.map = mMap!!

        // Request location updates

        val distance = 0F
        val timeUpdate = 0L

        // Request location updates via NETWORK
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, timeUpdate,distance, this)
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeUpdate,distance, this)
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

        //TODO: save my location
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) = logcat("onStatusChanged:  $provider")

    override fun onProviderDisabled(provider: String?) = logcat("onProviderDisabled:  $provider")

    override fun onProviderEnabled(provider: String?) = logcat("onProviderEnabled:  $provider")
}
