package com.pedromassango.herenow.ui.main.fragments.contacts

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Patterns
import android.view.*
import android.widget.RelativeLayout
import android.widget.Toast
import com.pedromassango.herenow.R
import com.pedromassango.herenow.app.HereNow
import com.pedromassango.herenow.data.RepositoryManager
import com.pedromassango.herenow.data.model.Contact
import com.pedromassango.herenow.data.preferences.PreferencesHelper
import com.pedromassango.herenow.extras.Utils
import kotlinx.android.synthetic.main.fragment_contacts.view.*
import android.provider.ContactsContract as DeviceContract


/**
 * Created by Pedro Massango on 12/28/17.
 */
class ContactsFragment : Fragment(), ContactsContract.View, (Int, Contact) -> Unit {

    // Static fields
    companion object {

        fun getInstance(): ContactsFragment {
            return ContactsFragment()
        }
    }

    //MVP
    private lateinit var presenter: ContactsPresenter

    // Contacts adapter
    private lateinit var contactsAdapter: ContactAdapter

    // Root view
    private lateinit var root: View

    // To check if has connection to internet
    override val isConnected: Boolean
        get() = Utils.isConnected(context!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable optionsMenu on this fragment
        setHasOptionsMenu(true)

        val preferencesHelper = PreferencesHelper(context!!.applicationContext)

        // Intialize presenter
        presenter = ContactsPresenter(this,
                RepositoryManager.contactsRepository(preferencesHelper),
                preferencesHelper)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_contacts, container, false)

        // Initialize adapter
        contactsAdapter = ContactAdapter(activity!!, this)

        // Swipe listener || Delete action
        val swipeHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val contact = contactsAdapter.getItem(position)

                //Remove item in adapter
                contactsAdapter.removeAt(position)

                // Call delete in Presenter
                presenter.onDeleteContact(contact, position)
            }
        }

        with(root) {

            // RecyclerView setup
            recycler_contacts.setHasFixedSize(true)
            recycler_contacts.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            recycler_contacts.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            recycler_contacts.adapter = contactsAdapter

            // Attach recyclerView on itemTouch listener, for swipe listener.
            val itemTouch = ItemTouchHelper(swipeHandler)
            itemTouch.attachToRecyclerView(recycler_contacts)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HereNow.logcat("ContactsFragment: onViewCreated.")

        // Load user contacts
        presenter.getUserContacts()
    }

    /**
     * Show received contacts from database  on List.
     * @param data the conact list to show
     */
    override fun showContact(data: MutableList<Contact>) {
        data.forEach { showContact(it) }
    }

    override fun showContact(data: Contact) {

        HereNow.logcat("showContact: onSuccess ->  $data")

        with(root) {
            tv_no_contacts.visibility = View.GONE
            progressbar_contacts.visibility = View.GONE
            recycler_contacts.visibility = View.VISIBLE
        }

        contactsAdapter.add(data)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        inflater!!.inflate(R.menu.fragment_contacts_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.action_add_contact -> {
                // Show dialog to enter a new contact
                showDialogNewContact()
            }
            R.id.action_import_contact -> {

                // Start contact picker intent
                val iPicker = Intent(Intent.ACTION_PICK, DeviceContract.CommonDataKinds.Phone.CONTENT_URI)
                startActivityForResult(iPicker, ContactsContract.RESULT_CONTACT_PICKER)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun showGetContactsProgress() {
        with(root) {
            recycler_contacts.visibility = View.GONE
            tv_no_contacts.visibility = View.VISIBLE
            tv_no_contacts.text = getString(R.string.getting_contacts)

            progressbar_contacts.visibility = View.VISIBLE
        }
    }

    override fun showGetContactsError() {
        with(root) {
            recycler_contacts.visibility = View.GONE
            progressbar_contacts.visibility = View.GONE
            tv_no_contacts.text = getString(R.string.get_contacts_error_click_toretry)
            tv_no_contacts.visibility = View.VISIBLE
            tv_no_contacts.setOnClickListener { presenter.getUserContacts() }
        }
    }

    override fun showNoContacts() {
        with(root) {
            recycler_contacts.visibility = View.GONE
            progressbar_contacts.visibility = View.GONE
            tv_no_contacts.text = getString(R.string.empty_contacts_info)
            tv_no_contacts.visibility = View.VISIBLE
            tv_no_contacts.setOnClickListener { showDialogNewContact() }
        }
    }

    override fun showSaveContactProgress() {
        with(root) {
            recycler_contacts.visibility = View.GONE
            tv_no_contacts.text = getString(R.string.saving_contact)
            tv_no_contacts.visibility = View.VISIBLE

            progressbar_contacts.visibility = View.VISIBLE
        }
    }

    override fun dismissSaveContactProgress() {
        with(root) {
            tv_no_contacts.visibility = View.GONE
            progressbar_contacts.visibility = View.GONE
        }
    }

    override fun showPleaseWaitMessage() = showToast(R.string.please_wait)

    override fun showSaveContactError() = showDialog(R.string.fail, R.string.unable_to_save_contact)

    override fun showPermissionUpdateSuccess() = showToast(R.string.permission_updated_success, Toast.LENGTH_LONG)

    override fun showContactDeletedMessage() {

        // Show contact delected
        showToast(R.string.contact_deleted, Toast.LENGTH_LONG)

        // Check if there is conctats on recyclerView
        if (contactsAdapter.itemCount == 0) {
            showNoContacts()
        }
    }

    override fun showDeleteErrorMessage() = showDialog(R.string.fail, R.string.delete_contact_error_message)

    override fun updateContactInAdapter(position: Int, contact: Contact) = contactsAdapter.update(position, contact)

    private fun showDialog(@StringRes title: Int, @StringRes message: Int) {
        val builder = AlertDialog.Builder(context!!)
        builder.setCancelable(false)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.str_ok, null)

        builder.create()
                .show()
    }

    private fun showDialogNewContact(name: String = "",
                                     number: String = "") {
        //  View for this Dialog
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_number, RelativeLayout(activity), false)
        val edtName = view.findViewById<TextInputLayout>(R.id.input_name)
        edtName.editText!!.setText(name)
        val edtNumber = view.findViewById<TextInputLayout>(R.id.input_number)
        edtNumber.editText!!.setText(number)

        // Building the input dialog
        val builder = AlertDialog.Builder(context!!)
        builder.setView(view)
        builder.setTitle(R.string.register_new_contact)
        builder.setNegativeButton(R.string.str_cancel, null)
        builder.setPositiveButton(R.string.str_save) { _, _ ->
            val name = edtName.editText!!.text.toString()
            val number = edtNumber.editText!!.text.toString()

            // Check entries
            if (validEntries(number)) {
                val dc = Contact(phoneNumber = number)

                dc.contactName = if (name.isEmpty()) "N/A" else name

                // Save the contact in server
                presenter.contactPicked(dc)
            }
        }

        builder.create()
                .show()

        showToast(R.string.add_country_code, Toast.LENGTH_LONG)
    }

    private fun validEntries(number: String): Boolean {
        if (!Patterns.PHONE.matcher(number).matches()) {
            showToast(R.string.phone_number_invalid, Toast.LENGTH_LONG)
            return false
        }
        return true
    }

    // Contact picker request result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ContactsContract.RESULT_CONTACT_PICKER) {
                val contact = toContact(data!!)
                if (contact != null) {
                    showDialogNewContact(contact.contactName, contact.phoneNumber)
                    //presenter.contactPicked(contact)
                } else {
                    showToast(R.string.something_was_wrong)
                }
            }
        }
    }

    // updatePermission switch clicked
    override fun invoke(mPosition: Int, mContact: Contact) {
        presenter.contactPermissionSwitched(mPosition, mContact)
    }

    /**
     * Query the Uri and read contact details, and return Contact model.
     * @param data the result to retrieve the contact picked info
     */
    private fun toContact(data: Intent): Contact? {
        val cursor: Cursor?
        try {
            // getData() method will have the Content Uri of the selected contact
            val uri = data.data
            //Query the content uri
            cursor = activity!!.contentResolver.query(uri, null, null, null, null)
            cursor!!.moveToFirst()
            // column index of the phone number
            val phoneIndex = cursor.getColumnIndex(DeviceContract.CommonDataKinds.Phone.NUMBER)
            // column index of the contact name
            val nameIndex = cursor.getColumnIndex(DeviceContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val phoneNo = cursor.getString(phoneIndex)
            val name = cursor.getString(nameIndex)

            // Close cursor
            cursor.close()

            return Contact(phoneNumber = phoneNo, contactName = name)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun showToast(@StringRes pick_contact_failed: Int, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(activity, pick_contact_failed, Toast.LENGTH_SHORT).show()
    }
}