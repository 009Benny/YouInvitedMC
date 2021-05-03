package com.example.youinvited.ui.editEvent

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.youinvited.R
import com.example.youinvited.models.EventClass
import com.example.youinvited.ui.eventList.EventListFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.edit_event_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.*
import java.io.File
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class EditEventFragment : Fragment() {
    private var isEditing:Boolean = false
    val args:EditEventFragmentArgs by navArgs()
    var id_event:String = ""
    val storageRef:StorageReference = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null

    companion object {
        fun newInstance() = EditEventFragment()
    }

    private lateinit var viewModel: EditEventViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        this.id_event = args.idEvent
        this.loadData(this.id_event)
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
                dateString = SimpleDateFormat("dd-MM-yyyy").format(date!!)
            }
            editTextEventName.setText(it.name)
            editTextEventAddress.setText(it.address)
            editTextInvitedCount.setText("$count")
            editTextEventDate.setText(dateString)
        })
        btnEditEvent.setOnClickListener{ this.btnEditAction() }
        btnCancel.setOnClickListener{ if (this.isEditing) this.btnUpdateEventAction() else this.btnCancelAction() }
        btnSelectImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data?.data
            imageViewMap.setImageURI(imageUri)
            uploadFile(imageUri)
        }
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
            storageRef.child("images/$event_id").downloadUrl.addOnSuccessListener {
                this.imageUri = it
                Picasso.get().load(it).into(imageViewMap)
            }.addOnFailureListener {
                print(it)
               //Toast.makeText(context, "No se pudo descargar la imagen", Toast.LENGTH_SHORT).show()
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
    }

    fun getDate(map:HashMap<String,Any>?):Date?{
        val day = map?.get("day") as? Long ?: 0
        val month = map?.get("month") as? Long ?: 0
        val year = map?.get("year") as? Long ?: 0
        val dateString = "$day-$month-$year"
        return SimpleDateFormat("dd-MM-yyyy").parse(dateString)
    }

    fun btnEditAction(){
        this.isEditing = !this.isEditing
        editTextEventName.isEnabled = this.isEditing
        editTextEventAddress.isEnabled = this.isEditing
        editTextInvitedCount.isEnabled = this.isEditing
        editTextEventDate.isEnabled = this.isEditing
        this.btnEditEvent.text = if (this.isEditing) getString(R.string.cancelButton) else getString(R.string.editButton)
        this.btnCancel.text = if (this.isEditing) getString(R.string.saveButton) else getString(R.string.regresarBtn)
    }

    fun btnUpdateEventAction(){
        val uid:String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        //this.id_event
        if (uid.length > 0 && this.id_event.length > 0) {
            val database = FirebaseDatabase.getInstance()
            val stringDate = editTextEventDate.text.toString()
            val eventDate = SimpleDateFormat("dd-MM-yyyy").parse(stringDate)
            val event = EventClass(id_event, uid, editTextEventName.text.toString(), editTextEventAddress.text.toString(), editTextInvitedCount.text.toString().toLong(), Date(), eventDate)
            val ref = database.getReference("Events")
            ref.child(uid).child(this.id_event).setValue(event)
            Toast.makeText(activity, "Se actualizo correctamente", Toast.LENGTH_SHORT).show()
            this.btnCancelAction()
        }else{
            Toast.makeText(activity, "No se pudo actualizar, intentelo de nuevo más tarde", Toast.LENGTH_SHORT).show()
        }
    }

    fun btnCancelAction(){
        findNavController().navigateUp()
    }

    fun uploadFile(path:Uri?){
        if (path != null){
            var pd = ProgressDialog(context)
            pd.setTitle("Uploading...")
            pd.show()
            //var ref = FirebaseStorage.getInstance().reference.child("images$id_event")
            var ref = FirebaseStorage.getInstance().reference.child("images").child("prueba1")
            ref.putFile(path).addOnSuccessListener {
                pd.dismiss()
                Toast.makeText(context, "File Uploaded", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                pd.dismiss()
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }.addOnProgressListener {
                var progress = (100 * it.bytesTransferred / it.totalByteCount)
                pd.setMessage("Uploading...${progress.toInt()}%")
            }
        }
    }

    fun showImageMapEditor(){
        if (this.imageUri != null){

        }else{
            Toast.makeText(context, "Necesitas seleccionar una imagen para poder editar el mapa", Toast.LENGTH_SHORT).show()
        }
    }

}