package com.example.youinvited.ui.eventList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youinvited.R
import com.example.youinvited.models.EventClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.event_list_fragment.*


class EventListFragment : Fragment(), EventListAdapter.OnItemClickListener {
    private var events:ArrayList<EventClass> = arrayListOf()
    private var myAdapter:EventListAdapter? = null
    private var database = FirebaseDatabase.getInstance()

    companion object {
        fun newInstance() = EventListFragment()
    }

    private lateinit var viewModel: EventListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        this.myAdapter = EventListAdapter(this.events, this)
        eventsRecycleView.adapter = this.myAdapter
    }

    fun loadEvents(){
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        if (uid.length > 0){
            val ref = database.getReference("Events")
            ref.child(uid).get().addOnSuccessListener {
                val mList: MutableList<EventClass> = ArrayList()
                for (unit in it.getChildren()) {
                    val value: EventClass? = unit.getValue(EventClass::class.java)
                    if (value != null) {
                        mList.add(value!!)
                    }
                }
                this.events = mList as ArrayList<EventClass>
                this.showNoEvents(this.events.count() == 0)
                this.myAdapter?.setData(this.events)
            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
        }
    }

    fun showNoEvents(show:Boolean){
        viewNoResults.visibility = if (show) View.VISIBLE else View.INVISIBLE
        textViewNoResults.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    fun showEventFragment(){

    }

    // EventListAdapter.onEventClickListener
    override fun showEvent(position: Int) {
        TODO("Not yet implemented")
        /*if (position < this.events.count()){
            val event = this.events[position]
            val name = event.name
            Toast.makeText(context, "$name", Toast.LENGTH_SHORT).show()
        }*/
    }

}