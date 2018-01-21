package com.pedromassango.herenow.data.remote

import android.os.AsyncTask
import com.google.android.gms.maps.model.LatLng
import com.pedromassango.herenow.R
import com.pedromassango.herenow.app.HereNow
import com.pedromassango.herenow.app.HereNow.Companion.logcat
import com.pedromassango.herenow.data.DataParser
import com.pedromassango.herenow.data.DownloadUrl
import com.pedromassango.herenow.data.NearbyPlacesDataSource


/**
 * Created by Pedro Massango on 1/21/18.
 */
class GetNearbyPlacesData(private val userLocation: LatLng,
                          private val placeType: String,
                          private val iRequestNearbyPlacesListener: NearbyPlacesDataSource.IRequestNearbyPlacesListener)
    : AsyncTask<Any, Any, String>() {

    private fun getUrl(): String {

        val googlePlacesUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
        googlePlacesUrl.append("location=${userLocation.latitude},${userLocation.longitude}")
        googlePlacesUrl.append("&radius=" + 5000)
        googlePlacesUrl.append("&type=" + placeType)
        googlePlacesUrl.append("&sensor=true")
        googlePlacesUrl.append("&key=AIzaSyAL6hW1s37z0n_tcNYVjieIlvwRLHi9OZ4")
        return googlePlacesUrl.toString()
    }

    override fun doInBackground(vararg params: Any?): String {
        logcat("getting neaby places...")
        return try {
            logcat("URL: -> ${getUrl()}")
            val requestUrl = getUrl()
            DownloadUrl.readUrl(requestUrl)
        } catch (e: Exception) {
            logcat("getting neaby places - error")
            e.printStackTrace()
            ""
        }
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        // Return if result is empty ( errors occours)
        if (result!!.isEmpty()) {
            iRequestNearbyPlacesListener.onError()
            return
        }

        // Convert string json result to Hash map places
        val unknowPlaceList = DataParser.parse(result)

        // Convert  hashMap places to Places data class list
        val placeList = DataParser.toPlaces(unknowPlaceList)
        iRequestNearbyPlacesListener.onSuccess(placeList)
    }

}