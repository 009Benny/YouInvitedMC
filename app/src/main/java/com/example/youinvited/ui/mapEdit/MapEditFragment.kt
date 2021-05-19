package com.example.youinvited.ui.mapEdit

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.example.youinvited.R
import com.example.youinvited.models.EventClass
import com.example.youinvited.models.InvitedClass
import com.example.youinvited.models.UserClass
import com.example.youinvited.ui.editEvent.EditEventFragmentArgs
import com.example.youinvited.ui.editEvent.EditEventFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.textFieldEmail
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.switchAdmin
import kotlinx.android.synthetic.main.map_edit_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.*

class MapEditFragment : Fragment(), MapEditAdapter.OnItemClickListener, MapEditDelegate {
    private val storageRef = FirebaseStorage.getInstance().reference
    private val database = FirebaseDatabase.getInstance()
    private var persons:ArrayList<InvitedClass> = arrayListOf()
    private var myAdapter:MapEditAdapter? = null
    private var imageUri: Uri? = null
    val args: MapEditFragmentArgs by navArgs()
    var id_event:String = ""
    var count_guests:Int = 0

    companion object {
        fun newInstance() = MapEditFragment()
    }

    private lateinit var viewModel: MapEditViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        this.id_event = args.idEvent
        this.count_guests = args.guestsCount
        this.loadImage()
        this.persons.add(InvitedClass("Uno"))
        this.persons.add(InvitedClass("Dos"))
        this.persons.add(InvitedClass("Tres"))
        this.persons.add(InvitedClass("Cuatro"))
        return inflater.inflate(R.layout.map_edit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mapView.delegate = this
        btnSaveChanges.setOnClickListener { saveChanges() }
        viewModel = ViewModelProvider(this).get(MapEditViewModel::class.java)
        this.initRecycle()
        this.downloadInviteds()
    }


    @SuppressLint("WrongConstant")
    fun initRecycle(){
        recyclerViewInviteds.layoutManager = LinearLayoutManager(this.context, OrientationHelper.HORIZONTAL, false)
        this.myAdapter = MapEditAdapter(this.persons, this)
        recyclerViewInviteds.adapter = this.myAdapter
    }

    override fun onSelectInvited(position: Int) {
        val invited = this.persons[position]
        mapView.showInvited(invited)
        Toast.makeText(context, "Se selecciono la persona: $position", Toast.LENGTH_SHORT).show()
    }

    override fun showCode(inv: InvitedClass) {
        val id = inv.location_id
        val link = "youinvited://$id"
        findNavController().navigate(MapEditFragmentDirections.actionMapEditFragmentToGenerateQRFragment(link))
    }

    fun loadImage(){
        storageRef.child("images/$id_event").downloadUrl.addOnSuccessListener {
            this.imageUri = it
            Picasso.get().load(it).into(imgMap)
        }.addOnFailureListener {
            print(it)
        }
    }

    fun downloadInviteds(){
        if (this.id_event.length > 0){
            val ref = database.getReference("Guests")
            ref.child(this.id_event).get().addOnSuccessListener {
                val mList: MutableList<InvitedClass> = ArrayList()
                for (unit in it.getChildren()) {
                    val value: InvitedClass? = unit.getValue(InvitedClass::class.java)
                    if (value != null) {
                        mList.add(value!!)
                    }
                }
                this.persons = mList as ArrayList<InvitedClass>
                if (this.persons.size == 0){
                    this.createInviteds()
                }else{
                    this.myAdapter?.setData(this.persons)
                }
            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
        }
    }

    fun createInviteds(){
        if (id_event.length > 0){
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val ref = database.getReference("Guests")
            val array:ArrayList<InvitedClass> = ArrayList()
            for (i in 0 until this.count_guests) {
                val name = i + 1
                val guest_id :String = "$id_event-guest-$i" ?: ""
                val guest = InvitedClass(
                        guest_id,
                        "guest$i",
                        id_event ?: "",
                        "Guest-$name"
                )
                ref.child(id_event ?:"").child(guest_id).setValue(guest)
                array.add(guest)
            }
            this.persons = array
            this.myAdapter?.setData(this.persons)
        }
    }

    override fun saveChanges() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Guests")
        if (this.persons.size > 0){
            var i = 0
            this.persons.forEach {
                ref.child(this.id_event).child("$id_event-guest-$i").setValue(it)
                i ++
            }
            Toast.makeText(activity, "Se actualizo correctamente", Toast.LENGTH_SHORT).show()
        }
    }

}
