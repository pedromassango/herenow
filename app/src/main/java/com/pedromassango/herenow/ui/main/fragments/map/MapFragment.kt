package com.pedromassango.herenow.ui.main.fragments.map

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
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

/**
 * Created by pedromassango on 12/28/17.
 */
class MapFragment : Fragment(), MapContract.View, OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    // If suspended, try to reconnect
    override fun onConnectionSuspended(p0: Int) = googleApiClient.connect()

    // If connection failed, print a message
    override fun onConnectionFailed(p0: ConnectionResult) = logcat("onConnectionFailed: $p0")

    companion object {
        var INSTANCE: MapFragment? = null

        fun getInstance(): MapFragment {
            if(INSTANCE == null){
                INSTANCE = MapFragment()
            }
            return INSTANCE!!
        }
    }

    // MVP
    lateinit var presenter: MapPresenter

    //Map
    lateinit var map: GoogleMap
    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var myLocationMarker: MarkerOptions
    private lateinit var myMarker: Marker

    // TO update marker position
    private var arleadySet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setup GoogleApiClient and location request
        // Setup  googleApiClient
        googleApiClient = GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        // Location request, for location updates
        locationRequest = LocationRequest()
        locationRequest.interval = 60000 // 1min
        locationRequest.fastestInterval = 30000 // 30sec
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        // Prepare user marker on map
        myLocationMarker = MarkerOptions()

        myLocationMarker.title(getString(R.string.you_are_here))
        myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on))
        myLocationMarker.draggable(false)
        myLocationMarker.flat(true)

        // Setup presenter
        presenter = MapPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_maps, container, false)

        // Setting up MapFragment
        val mapFragment = activity.supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onMapReady(mMap: GoogleMap?) {
        logcat("onMapReady")
        this.map = mMap!!

        // Connect to googleApiClient to request location
        googleApiClient.connect()
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        // Once connected, to google API, request location updates
        logcat("onConnected - requestLocationUpdates")

        // Location request listener
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest) { location ->
            // Location updates
            logcat("onLocationChanged")

            if(arleadySet){
                // set the marker at first time
                myLocationMarker.position(LatLng(location.latitude, location.longitude))
                myMarker = map.addMarker(myLocationMarker)
                arleadySet = true
            }else{
                // Update the hold position to a recent position
                myMarker.position = LatLng(location.latitude, location.longitude)
            }

            //TODO: save my location
        }
    }
}