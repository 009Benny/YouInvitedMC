package com.example.youinvited

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.youinvited.models.UserClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.textFieldEmail
import kotlinx.android.synthetic.main.activity_login.textFieldPass
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        this.loadLocate()
        setContentView(R.layout.activity_register)
        title = "Registrarse"
        btn_register.setOnClickListener { this.registerUser() }
    }

    fun registerUser(){
        if (textFieldEmail.text.isNotEmpty() && textFieldPass.text.isNotEmpty() && textFieldName.text.isNotEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(textFieldEmail.text.toString(),textFieldPass.text.toString()).addOnCompleteListener {
                if (it.isSuccessful){
                    FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                    Toast.makeText(this, "Se envio un email de verificación a tu correo", Toast.LENGTH_SHORT)
                    this.crateUserDocument()
                    this.showHome()
                }else{
                    this.showAlert(it.exception.toString())
                }
            }
        }else{
            Toast.makeText(this, "Favor de llenar al menos Nombre, email y contraseña", Toast.LENGTH_SHORT)
        }
    }

    fun showAlert(message:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        if (message.contains("is alredy in use")){
            builder.setMessage("El email ya es usado por otro usuario")
        }else {
            builder.setMessage("Se ha producido un error al registrar usuario")
        }
        builder.setPositiveButton("Aceptar", null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }

    fun showHome(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun crateUserDocument(){
        val user_id:String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val email:String = textFieldEmail.text.toString()
        if (email.length > 0 && user_id.length > 0) {
            val database = FirebaseDatabase.getInstance()
            val user = UserClass(user_id, textFieldName.text.toString(), email, textFieldAddress.text.toString(), textFieldPhone.text.toString(), switchAdmin.isChecked)
            val ref = database.getReference("Users")
            ref.child(user_id).setValue(user)
        }
    }

}