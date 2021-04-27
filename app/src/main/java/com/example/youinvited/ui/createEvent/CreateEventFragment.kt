package com.example.youinvited.ui.createEvent

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.example.youinvited.R
import com.example.youinvited.extensions.DatePickerFragment
import com.example.youinvited.models.EventClass
import com.example.youinvited.ui.profile.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.create_event_fragment.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CreateEventFragment : Fragment() {

    companion object {
        fun newInstance() = CreateEventFragment()
    }

    private lateinit var viewModel: CreateEventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_event_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreateEventViewModel::class.java)
        // TODO: Use the ViewModel
        btnSave.setOnClickListener { this.btnCrateEventAction() }
        btnCancel.setOnClickListener { this.btnCancelAction() }
        editTextEventDate.setOnClickListener { this.showDateDialog() }
    }

    fun btnCancelAction(){
        findNavController().navigateUp()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun btnCrateEventAction(){
        val uid:String = FirebaseAuth.getInstance().currentUser?.uid.toString() ?: ""
        val name = editTextEventName.text.toString()
        val eventId = uid + name.toLowerCase().replace(" ", "_")
        val address = editTextEventAddress.text.toString()
        val places = emptyArray<String>()
        val invCount = editTextInvitedCount.text.toString().toLong()
        val creationDate = Date()
        val string = editTextEventDate.text.toString()
        val eventDate = SimpleDateFormat("dd-MM-yyyy").parse(string)
        if (uid.length > 0 && name.length > 0 && address.length > 0 && invCount > 0 && string.length > 0){
            val database = FirebaseDatabase.getInstance()
            val event = EventClass(eventId, uid, name, address, invCount, creationDate, eventDate)
            val ref = database.getReference("Events")
            ref.child(uid).child(eventId).setValue(event)
            Toast.makeText(activity, "Se registro correctamente el evento", Toast.LENGTH_SHORT)
            findNavController().navigateUp()
        }else{
            Toast.makeText(activity, "Favor de rellenar todos los campos", Toast.LENGTH_SHORT)
        }
    }

    fun showDateDialog(){
        val datePicker = DatePickerFragment{ day, month, year -> onDateSelected(day, month, year) }
        activity?.supportFragmentManager?.let { datePicker.show(it, "datePicker") }
    }

    fun onDateSelected(day:Int, month:Int, year:Int){
        editTextEventDate.setText("$day-$month-$year")
    }

}