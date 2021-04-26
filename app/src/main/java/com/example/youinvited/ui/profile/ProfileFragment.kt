package com.example.youinvited.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.youinvited.R
import com.example.youinvited.models.UserClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.switchAdmin

class ProfileFragment : Fragment() {
    var edit:Boolean = false

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.loadProfile()
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.user_Data.observe(viewLifecycleOwner, Observer { it ->
            editTextName.setText(it.username)
            editTextEmail.setText(it.email)
            editTextAddress.setText(it.address)
            editTextPhone.setText(it.phone)
            switchAdmin.isChecked = it.admin
        })
        btnEditProfile.setOnClickListener { this.btnEditAction() }
        btnCreateEvent.setOnClickListener { if (this.edit) this.btnUpdateProfileAcction() else this.btnCreateEventAction() }
    }

    fun loadProfile(){
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        if (uid.length > 0){
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("Users")
            ref.child(uid).get().addOnSuccessListener {
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
        viewModel.user_Data.value = UserClass(map["uid"] as? String ?: "", map["username"] as? String ?: "", map["email"] as? String ?: "", map["address"] as? String ?: "", map["phone"] as? String ?: "", map["admin"] as? Boolean ?: false)
        btnCreateEvent.isVisible = viewModel.user_Data.value?.admin ?: false
    }

    // MARK: - Btn Actions
    fun btnEditAction(){
        this.edit = !this.edit
        editTextName.isEnabled = this.edit
        editTextEmail.isEnabled = this.edit
        editTextAddress.isEnabled = this.edit
        editTextPhone.isEnabled = this.edit
        //switchAdmin.isEnabled = this.edit
        this.btnEditProfile.text = if (this.edit) getString(R.string.cancelButton) else getString(R.string.editButton)
        this.btnCreateEvent.text = if (this.edit) getString(R.string.updateProfileButton) else getString(R.string.createEvent)
    }

    fun btnCreateEventAction(){
        findNavController().navigate(ProfileFragmentDirections.actionNavProfileToCreateEventFragment())
    }

    fun btnUpdateProfileAcction(){
        val uid:String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val email:String = editTextEmail.text.toString()
        if (email.length > 0 && uid.length > 0) {
            val database = FirebaseDatabase.getInstance()
            val user = UserClass(uid, editTextName.text.toString(), email, editTextAddress.text.toString(), editTextPhone.text.toString(), switchAdmin.isChecked)
            val ref = database.getReference("Users")
            //ref.child(user_id).setValue(user)
            ref.child(uid).setValue(user)
            Toast.makeText(activity, "Se actualizo correctamente", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(activity, "No se pudo actualizar, intentelo de nuevo más tarde", Toast.LENGTH_SHORT).show()
        }

    }

}