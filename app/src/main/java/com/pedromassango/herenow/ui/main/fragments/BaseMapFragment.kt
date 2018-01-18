package com.pedromassango.herenow.ui.main.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.pedromassango.herenow.R
import com.pedromassango.herenow.app.HereNow.Companion.logcat
import kotlinx.android.synthetic.main.fragment_maps.view.*

/**
 * Created by pedromassango on 12/28/17.
 *
 * Show the Map with friends location (If available)
 */
abstract class BaseMapFragment : Fragment(), OnMapReadyCallback {

    //Map
    var map: GoogleMap? = null
    lateinit var mMapView: MapView

    // View
    lateinit var root: View

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
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mMapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        mMapView.onDestroy()
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater!!.inflate(R.layout.fragment_maps, container, false)

        mMapView = root.maps_view
        mMapView.onCreate(savedInstanceState)
        mMapView.onResume() // To setup Map immediately

        try {
            MapsInitializer.initialize(activity.applicationContext)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        mMapView.getMapAsync(this)

        return root
    }

    fun Loader() {
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

    override fun onMapReady(mMap: GoogleMap?) {
        logcat("onMapReady")

        // Get map reference
        this.map = mMap!!

        // Setting up map settings
        mMap.uiSettings?.isIndoorLevelPickerEnabled = true
        mMap.isBuildingsEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.setMinZoomPreference(11F)
    }
}
