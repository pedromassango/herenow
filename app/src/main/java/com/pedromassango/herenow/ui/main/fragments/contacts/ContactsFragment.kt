package com.pedromassango.herenow.ui.main.fragments.contacts

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.pedromassango.herenow.R
import com.pedromassango.herenow.data.model.Contact
import kotlinx.android.synthetic.main.fragment_contacts.view.*

import android.provider.ContactsContract as DeviceContract
import android.provider.ContactsContract.CommonDataKinds
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.util.Patterns


/**
 * Created by pedromassango on 12/28/17.
 */
class ContactsFragment : Fragment(), ContactsContract.View {

    // Static fields
    companion object {
        var DUMMY : ArrayList<Contact> = ArrayList()

        init {
            DUMMY.add( Contact(contactName =  "Pedro Massango",phoneNumber =  "948 020 308",lat =  0.0, lng =  0.0))
            DUMMY.add( Contact(contactName =  "Anisio Isidoro",phoneNumber =  "923 123 463",lat =  0.0, lng =  0.0))
            DUMMY.add( Contact(contactName =  "Mendes Massango",phoneNumber =  "910 527 624",lat =  0.0, lng =  0.0))
            DUMMY.add( Contact(contactName =  "Pedro Massango",phoneNumber =  "948 020 308",lat =  0.0, lng =  0.0))
            DUMMY.add( Contact(contactName =  "Suraia Gourgel",phoneNumber =  "928 573 178",lat =  0.0, lng =  0.0))
            DUMMY.add( Contact(contactName =  "Anisio Isidoro",phoneNumber =  "923 123 463",lat =  0.0, lng =  0.0))
        }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable optionsMenu on this fragment
        setHasOptionsMenu(true)

        // Intialize presenter
        presenter = ContactsPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater!!.inflate(R.layout.fragment_contacts, container, false)

        // Initialize adapter
        contactsAdapter = ContactAdapter()

        with(root) {
            tv_no_contacts.setTextColor(resources.getColor(R.color.white))

            // RecyclerView setup
            recycler_contacts.setHasFixedSize(true)
            recycler_contacts.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            recycler_contacts.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            recycler_contacts.adapter = contactsAdapter
        }

        return root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: show DUMMY data, just for tests
        showContacts(DUMMY)
    }

    override fun showContacts(data : ArrayList<Contact>) {
        with(root){
            tv_no_contacts.visibility = View.GONE
            progressbar_contacts.visibility = View.GONE
        }

        contactsAdapter.add(data)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        inflater!!.inflate(R.menu.fragment_peoples_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){
            R.id.action_add_contact ->{
                // Show dialog to enter a new contact
                showDialogNewContact()
            }
            R.id.action_import_contact ->{

                // Start contact picker intent
                val iPicker = Intent(Intent.ACTION_PICK, DeviceContract.CommonDataKinds.Phone.CONTENT_URI)
                startActivityForResult(iPicker, ContactsContract.RESULT_CONTACT_PICKER)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showDialogNewContact() {
        //  View for this Dialog
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_number, null, false)
        val edtName = view.findViewById<TextInputLayout>(R.id.input_name)
        val edtNumber = view.findViewById<TextInputLayout>(R.id.input_number)

        // Building the input dialog
        val builder = AlertDialog.Builder(context)
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
    }

    private fun validEntries(number: String): Boolean {
        if (!Patterns.PHONE.matcher(number).matches()) {
            showToast(R.string.phone_number_invalid)
            return false
        }
        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == ContactsContract.RESULT_CONTACT_PICKER){
                val contact = toContact(data!!)
                if(contact != null) {
                    presenter.contactPicked( contact)
                }else{
                    showToast(R.string.something_was_wrong)
                }
            }
        }else{
            showToast(R.string.pick_contact_failed)
        }
    }

    /**
     * Query the Uri and read contact details, and return Contact model.
     * @param data the result to retrieve the contact picked info
     */
    private fun toContact(data: Intent): Contact? {
        var cursor: Cursor? = null
        try {
            // getData() method will have the Content Uri of the selected contact
            val uri = data.data
            //Query the content uri
            cursor = activity.contentResolver.query(uri, null, null, null, null)
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

    private fun showToast(@StringRes pick_contact_failed: Int) {
        Toast.makeText(activity, pick_contact_failed, Toast.LENGTH_SHORT).show()
    }
}