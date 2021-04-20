package com.example.youinvited.ui.eventList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youinvited.R
import com.example.youinvited.models.EventClass
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.event_list_fragment.*


class EventListFragment : Fragment() {
    private var events:ArrayList<EventClass> = arrayListOf()
    private var myAdapter:EventListAdapter? = null

    companion object {
        fun newInstance() = EventListFragment()
    }

    private lateinit var viewModel: EventListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.events.add(EventClass("eventid1", "userID1", "Mi primer evento", "Esta es la direccion"))
        this.events.add(EventClass("eventid2", "userID2", "Mi segundo evento", "Esta es la otra direccion"))
        return inflater.inflate(R.layout.event_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EventListViewModel::class.java)
        // TODO: Use the ViewModel
        this.initRecycle()

        this.loadEvents()
    }

    fun initRecycle(){
        eventsRecycleView.layoutManager = LinearLayoutManager(this.context)
        this.myAdapter = EventListAdapter(this.events)
        eventsRecycleView.adapter = this.myAdapter
    }

    fun loadEvents(){
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Events")
        ref.get().addOnSuccessListener {
            val mList: MutableList<EventClass> = ArrayList()
            for (unit in it.getChildren()) {
                val value: EventClass? = unit.getValue(EventClass::class.java)
                if (value != null) {
                    mList.add(value!!)
                }
            }
            this.events = mList as ArrayList<EventClass>
            this.myAdapter?.setData(this.events)
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

}