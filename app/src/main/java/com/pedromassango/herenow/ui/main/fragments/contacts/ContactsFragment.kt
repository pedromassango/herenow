package com.pedromassango.herenow.ui.main.fragments.contacts

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.pedromassango.herenow.R
import com.pedromassango.herenow.data.model.Contact
import kotlinx.android.synthetic.main.fragment_contacts.view.*

/**
 * Created by pedromassango on 12/28/17.
 */
class ContactsFragment : Fragment() {

    companion object {
        var DUMMY : ArrayList<Contact> = ArrayList()

        init {
            DUMMY.add( Contact(contactName =  "Pedro Massango",phoneNumber =  "948 020 308",lat =  0.0, lng =  0.0))
            DUMMY.add( Contact(contactName =  "Anisio Isidoro",phoneNumber =  "923 123 463",lat =  0.0, lng =  0.0))
            DUMMY.add( Contact(contactName =  "Mendes Massango",phoneNumber =  "910 527 624",lat =  0.0, lng =  0.0))
            DUMMY.add( Contact(contactName =  "Pedro Massango",phoneNumber =  "948 020 308",lat =  0.0, lng =  0.0))
            DUMMY.add( Contact(contactName =  "Suraia Gourgel",phoneNumber =  "928 573 178",lat =  0.0, lng =  0.0))
            DUMMY.add( Contact(contactName =  "Anisio Isidoro",phoneNumber =  "923 123 463",lat =  0.0, lng =  0.0))
            DUMMY.add( Contact(contactName =  "Januario Machado",phoneNumber =  "934 783 328",lat =  0.0, lng =  0.0))
            DUMMY.add( Contact(contactName =  "Suraia Gourgel",phoneNumber =  "928 573 178",lat =  0.0, lng =  0.0))
            DUMMY.add( Contact(contactName =  "Mendes Massango",phoneNumber =  "910 527 624",lat =  0.0, lng =  0.0))
        }

        private var INSTANCE: ContactsFragment? = null

        fun getInstance(): ContactsFragment {
            if (INSTANCE == null) {
                INSTANCE = ContactsFragment()
            }
            return INSTANCE!!
        }
    }

    // Contacts adapter
    lateinit var contactsAdapter: ContactAdapter

    // Root view
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable optionsMenu on this fragment
        setHasOptionsMenu(true)
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

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: show DUMMY data, just for tests
        showContacts(DUMMY)
    }

    fun showContacts(data : ArrayList<Contact>) {
       /* with(root){
            tv_no_contacts.visibility = View.GONE
            progressbar_contacts.visibility = View.GONE
        }*/

        contactsAdapter.add(data)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        inflater!!.inflate(R.menu.fragment_peoples_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item!!.itemId == R.id.action_add_contact) {
            //TODO: handle impor contact click
            Toast.makeText(activity, "Adicionar contacto", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }
}