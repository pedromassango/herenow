package com.pedromassango.herenow.ui.main.fragments.map

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pedromassango.herenow.R

/**
 * Created by pedromassango on 12/28/17.
 */
class MapFragment : Fragment() {

    companion object {
        var INSTANCE: MapFragment? = null

        fun getInstance(): MapFragment {
            if(INSTANCE == null){
                INSTANCE = MapFragment()
            }
            return INSTANCE!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_maps, container, false)

        return view
    }
}