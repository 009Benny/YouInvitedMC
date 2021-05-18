package com.example.youinvited.ui.showQRPlace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.youinvited.R
import com.example.youinvited.models.InvitedClass
import com.example.youinvited.ui.mapEdit.MapView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_show_place_ativity.*

class ShowPlaceAtivity : AppCompatActivity() {
    private val storageRef = FirebaseStorage.getInstance().reference
    var invited_id:String = ""
    var uid:String = ""
    var event_id:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_place_ativity)
        var message = intent.getStringExtra("qr_code") ?:""
        message = message.replace("youinvited://", "")
        this.uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        this.invited_id = message ?: ""
        val aux = invited_id.split("-")
        if (aux.size > 0) {
            this.event_id = aux.get(0)
        }
        eventNameTextView.text = this.invited_id
        this.loadEventPlace()
    }

    fun loadEventPlace(){
        if (this.uid.length > 0 && this.event_id.length > 0){
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("Guests")
            this.loadImage(this.event_id)
            ref.child(this.event_id).child(this.invited_id).get().addOnSuccessListener {
                val map = it.getValue() as? HashMap<String, Any>
                if (map != null){
                    val x = map.get("x")
                    val y = map.get("y")
                    val invited = InvitedClass(
                            map.get("location_id").toString(),
                            map.get("invited_id").toString(),
                            map.get("event_id").toString(),
                            map.get("public_name").toString(),
                            0.0,
                            0.0
                    )
                    mapView.showInvited(invited)
                }
            }.addOnFailureListener {
                print("here")
            }
        }
    }

    fun loadImage(src:String){
        if (src.length > 0){
            storageRef.child("images/$src").downloadUrl.addOnSuccessListener {
                Picasso.get().load(it).into(imgMap)
            }.addOnFailureListener {
                print(it)
            }
        }

    }


}