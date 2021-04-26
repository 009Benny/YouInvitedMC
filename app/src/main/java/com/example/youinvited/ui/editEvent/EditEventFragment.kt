package com.example.youinvited.ui.editEvent

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.youinvited.R
import com.example.youinvited.models.EventClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.edit_event_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class EditEventFragment : Fragment() {
    private var isEditing:Boolean = false
    val args:EditEventFragmentArgs by navArgs()

    companion object {
        fun newInstance() = EditEventFragment()
    }

    private lateinit var viewModel: EditEventViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val id_event = args.idEvent
        this.loadData(id_event)
        return inflater.inflate(R.layout.edit_event_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditEventViewModel::class.java)
        viewModel.event_Data.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val count = it.countInvited
            val date = it.eventDate
            var dateString:String = ""
            if (date != null){
                dateString = SimpleDateFormat("dd - MM - yyyy").format(date!!)
            }
            editTextEventName.setText(it.name)
            editTextEventAddress.setText(it.address)
            editTextInvitedCount.setText("$count")
            editTextEventDate.setText(dateString)
        })
    }

    fun loadData(event_id:String){
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        if (uid.length > 0){
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("Events")
            ref.child(uid).child(event_id).get().addOnSuccessListener {
                val map = it.getValue() as? HashMap<String, Any>
                if (map != null){
                    this.updateData(map)
                }
            }.addOnFailureListener {
                print("here")
            }
        }
    }

    fun updateData(map: HashMap<String,Any>){
        val eventDateMap = map["modificationDate"] as? HashMap<String,Any>
        val modificationDateMap = map["modificationDate"] as? HashMap<String,Any>
        viewModel.event_Data.value = EventClass(
            map["event_id"] as? String ?: "",
            map["user_id"] as? String ?: "",
            map["name"] as? String ?: "",
            map["address"] as? String ?: "",
            map["countInvited"] as? Long ?: 0,
            getDate(modificationDateMap) as? Date ?: null,
            getDate(eventDateMap) as? Date ?: null
        )
        //btnCreateEvent.isVisible = vixewModel.user_Data.value?.admin ?: false
    }

    fun getDate(map:HashMap<String,Any>?):Date?{
        val day = map?.get("day") as? Long ?: 0
        val month = map?.get("month") as? Long ?: 0
        val year = map?.get("year") as? Long ?: 0
        val dateString = "$day-$month-$year"
        return SimpleDateFormat("dd-MM-yyyy").parse(dateString)
    }

}