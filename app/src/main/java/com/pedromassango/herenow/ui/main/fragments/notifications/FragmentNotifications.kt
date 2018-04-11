package com.pedromassango.herenow.ui.main.fragments.notifications

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pedromassango.herenow.R
import com.pedromassango.herenow.data.ContactsDataSource
import com.pedromassango.herenow.data.ContactsRepository
import com.pedromassango.herenow.data.model.Contact
import com.pedromassango.herenow.data.model.Notification
import com.pedromassango.herenow.data.preferences.PreferencesHelper
import com.pedromassango.herenow.data.remote.ContactsRemoteRepository
import kotlinx.android.synthetic.main.fragment_contacts.view.*

/**
 * Created by Pedro Massango on 4/11/18.
 */
class FragmentNotifications : Fragment(), (Int, Notification, Boolean) -> Unit {

    private lateinit var repository: ContactsRepository

    private lateinit var adapter: NotificationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repository = ContactsRepository(
                ContactsRemoteRepository(
                        PreferencesHelper(context!!)
                )
        )

        adapter = NotificationsAdapter(context!!, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        with(view) {

            // RecyclerView setup
            recycler_contacts.setHasFixedSize(true)
            recycler_contacts.layoutManager = android.support.v7.widget.LinearLayoutManager(activity, android.support.v7.widget.LinearLayoutManager.VERTICAL, false)
            recycler_contacts.addItemDecoration(android.support.v7.widget.DividerItemDecoration(activity, android.support.v7.widget.DividerItemDecoration.VERTICAL))
            recycler_contacts.adapter = adapter
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // show a progress bar
        showGetNotificationsProgress()

        repository.getNotifications({
            if(it.isEmpty()){
                showNoNotifications()
            }else{
                removeProgress()
                adapter.add(it)
            }
        })
    }

    override fun invoke(position: Int, notification: Notification, allow: Boolean) {

        // Show a progress message
        showToast(R.string.please_wait, Toast.LENGTH_LONG)

        val contact = Contact()
        contact.phoneNumber = notification.authorNumber
        contact.contactName = notification.authorName
        contact.allow = allow
        repository.updatePermission(position,contact, object : ContactsDataSource.IResultListener{
            override fun onSuccess(position: Int) {

                // update notification item, and show it
                notification.allowed = true

                // Update in adapter
                adapter.update(position, notification)

                // Also update notification in sever
                repository.sendNotification(true, notification)

                // show a success message
                showToast(R.string.permission_updated_success, Toast.LENGTH_LONG)
            }

            override fun onError(position: Int) {
                showDialog(R.string.fail, R.string.update_permission_error_message)
            }
        })
    }

    private fun showGetNotificationsProgress() {
        with(view!!) {
            recycler_contacts.visibility = android.view.View.GONE
            tv_no_contacts.visibility = android.view.View.VISIBLE
            tv_no_contacts.text = getString(com.pedromassango.herenow.R.string.please_wait)

            progressbar_contacts.visibility = android.view.View.VISIBLE
        }
    }

    private fun showNoNotifications() {
        with(view!!) {
            recycler_contacts.visibility = android.view.View.GONE
            progressbar_contacts.visibility = android.view.View.GONE
            tv_no_contacts.text = getString(com.pedromassango.herenow.R.string.your_notifications_appear_here)
            tv_no_contacts.visibility = android.view.View.VISIBLE
        }
    }

    private fun removeProgress() {
        with(view!!) {
            recycler_contacts.visibility = android.view.View.VISIBLE
            tv_no_contacts.visibility = android.view.View.GONE
            progressbar_contacts.visibility = android.view.View.GONE
        }
    }

    private fun showDialog(@StringRes title: Int, @StringRes message: Int) {
        val builder = AlertDialog.Builder(context!!)
        builder.setCancelable(false)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.str_ok, null)

        builder.create()
                .show()
    }

    private fun showToast(@StringRes pick_contact_failed: Int, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(activity, pick_contact_failed, Toast.LENGTH_SHORT).show()
    }
}