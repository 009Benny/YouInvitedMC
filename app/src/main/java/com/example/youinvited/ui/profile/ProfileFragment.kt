package com.example.youinvited.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.youinvited.ProviderType
import com.example.youinvited.R
import com.example.youinvited.models.UserClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.updateProfile()
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
    }

    fun updateProfile(){
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

            }
        }
    }

    fun updateData(map: HashMap<String,Any>){
        viewModel.user_Data.value = UserClass(map["uid"] as? String ?: "", map["username"] as? String ?: "", map["email"] as? String ?: "", map["address"] as? String ?: "", map["phone"] as? String ?: "", map["admin"] as? Boolean ?: false)
    }

}