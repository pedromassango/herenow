package com.pedromassango.herenow.data.remote

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.pedromassango.herenow.data.model.Contact

/**
 * Created by Pedro Massango on 1/12/18.
 */
class IValueEventListener(val myValueListener: MyValueListener?) : ValueEventListener {

    var myLocationListener: MyLocationListener? = null

    constructor(myLocationListener: MyLocationListener) : this(null) {
        this.myLocationListener = myLocationListener
    }

    override fun onCancelled(p0: DatabaseError?) {

        myLocationListener?.success(null)
        myValueListener?.success(null)
    }

    override fun onDataChange(p0: DataSnapshot?) {
        if (p0 == null || !p0.exists()) {
            myValueListener?.success(null)
            myLocationListener?.success(null)
            return
        }

        val contact = p0.getValue(Contact::class.java)
        myValueListener?.success(contact)
        myLocationListener?.success(p0)
    }
}

interface MyValueListener {
    fun success(contact: Contact?)
}

interface MyLocationListener {
    fun success(p0: DataSnapshot?)
}