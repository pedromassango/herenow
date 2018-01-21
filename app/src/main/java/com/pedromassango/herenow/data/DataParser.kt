package com.pedromassango.herenow.data

import android.util.Log
import com.pedromassango.herenow.data.model.Place
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONStringer


/**
 * Created by Pedro Massango on 1/21/18.
 */
object DataParser {
    fun parse(jsonData: String): List<HashMap<String, String>> {
        var jsonArray: JSONArray? = null

        try {
            Log.d("Places", "parse")
            val jsonObject = JSONObject(jsonData)
            jsonArray = jsonObject.getJSONArray("results")
        } catch (e: JSONException) {
            Log.d("Places", "parse error")
            e.printStackTrace()
        }

        return getPlaces(jsonArray)
    }

    private fun getPlaces(jsonArray: JSONArray?): List<HashMap<String, String>> {
        val placesCount = jsonArray!!.length()
        val placesList = arrayListOf<HashMap<String, String>>()
        var placeMap: HashMap<String, String>? = null
        Log.d("Places", "getPlaces")

        for (i in 0 until placesCount) {
            try {
                placeMap = getPlace(jsonArray.get(i) as JSONObject)
                placesList.add(placeMap)
                Log.d("Places", "Adding places")

            } catch (e: JSONException) {
                Log.d("Places", "Error in Adding places")
                e.printStackTrace()
            }

        }
        return placesList
    }

    private fun getPlace(googlePlaceJson: JSONObject): HashMap<String, String> {
        val googlePlaceMap = HashMap<String, String>()
        var placeName = "-NA-"
        var vicinity = "-NA-"
        var latitude = ""
        var longitude = ""
        var reference = ""

        Log.d("getPlace", "Entered")

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name")
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity")
            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat")
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng")
            reference = googlePlaceJson.getString("reference")
            googlePlaceMap.put("place_name", placeName)
            googlePlaceMap.put("vicinity", vicinity)
            googlePlaceMap.put("lat", latitude)
            googlePlaceMap.put("lng", longitude)
            googlePlaceMap.put("reference", reference)
            Log.d("getPlace", "Putting Places")
        } catch (e: JSONException) {
            Log.d("getPlace", "Error")
            e.printStackTrace()
        }

        return googlePlaceMap
    }

    fun toPlaces(unknowPlaceList: List<HashMap<String, String>>): ArrayList<Place> {
        val output = arrayListOf<Place>()

        unknowPlaceList.forEach {unknowPlace ->
            val placeName = unknowPlace.get("place_name")!!
            val vicinity = unknowPlace.get("vicinity")!!
            val lat = unknowPlace.get("lat")!!.toDouble()
            val lng = unknowPlace.get("lng")!!.toDouble()

            val place = Place(placeName, vicinity = vicinity, lat = lat, lng = lng)
            output.add( place)
        }
        return output
    }
}