package com.pedromassango.herenow.ui.main.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.pedromassango.herenow.R
import com.pedromassango.herenow.app.HereNow.Companion.logcat
import com.pedromassango.herenow.ui.main.IPermissionListener
import kotlinx.android.synthetic.main.fragment_maps.view.*

/**
 * Created by pedromassango on 12/28/17.
 *
 * Show the Map with friends location (If available)
 */
abstract class BaseMapFragment : Fragment(), OnMapReadyCallback, LocationListener {

    //Map
    var map: GoogleMap? = null
    lateinit var mMapView: MapView


    // TO request device location updates
    lateinit var locationManager: LocationManager

    // Location updates delay and distance
    private val distance = 20F
    var timeUpdate = 5000L // 5sec

    // View
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup locationManager
        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_maps, container, false)

        mMapView = root.maps_view
        mMapView.onCreate(savedInstanceState)
        mMapView.onResume() // To setup Map immediately

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        mMapView.getMapAsync(this)

        return root
    }

    override fun onStart() {
        super.onStart()
        mMapView.onStart()
    }

    override fun onStop() {
        mMapView.onStop()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onPause() {
        mMapView.onPause()
        super.onPause()

        // Remove location updates
        locationManager.removeUpdates(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }

    override fun onDestroy() {
        mMapView.onDestroy()
        super.onDestroy()
    }

    fun loader() {
        with(root) {
            mMapView.visibility = View.GONE
            progressbar_maps.visibility = View.VISIBLE
            tv_map_info.visibility = View.VISIBLE
            tv_map_info.text = getString(R.string.please_wait)
        }
    }

    fun dismissLoader() {
        with(root) {
            mMapView.visibility = View.VISIBLE
            progressbar_maps.visibility = View.GONE
            tv_map_info.visibility = View.GONE
        }
    }

    fun requestLocationPermission(iPermissionListener: IPermissionListener) {

        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener {

                    override fun onPermissionGranted(response: PermissionGrantedResponse?) = iPermissionListener.invoke(true)

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                        val dialog = AlertDialog.Builder(activity!!)
                                .setTitle(R.string.request_location_permission_title)
                                .setMessage(R.string.request_location_permission_message)
                                .setCancelable(false)
                                .setPositiveButton(R.string.str_ok) { _, _ -> requestLocationPermission(iPermissionListener) }

                        dialog.create().show()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) =
                            iPermissionListener.invoke(false)
                }).check()
    }

    override fun onMapReady(mMap: GoogleMap?) {
        logcat("onMapReady")

        // Get map reference
        this.map = mMap!!

        // Setting up map settings
        //mMap.uiSettings?.isIndoorLevelPickerEnabled = true
        //mMap.isBuildingsEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        mMap.setMinZoomPreference(10.0F)
        //mMap.setMaxZoomPreference(20.0F)

        requestLocationPermission(object : IPermissionListener {
            @SuppressLint("MissingPermission")
            override fun invoke(state: Boolean) {
                when (state) {
                    false -> activity!!.finish()
                    true -> {

                        // Request location updates via GPS
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeUpdate, distance, this@BaseMapFragment)
                        // Request location updates via PASSIVE-PROVIDER
                        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, timeUpdate, distance, this@BaseMapFragment)
                        // Request location updates via NETWORK
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, timeUpdate, distance, this@BaseMapFragment)
                    }
                }
            }
        })
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) = logcat("onStatusChanged:  $provider")

    override fun onProviderDisabled(provider: String?) = logcat("onProviderDisabled:  $provider")

    override fun onProviderEnabled(provider: String?) = logcat("onProviderEnabled:  $provider")
}
