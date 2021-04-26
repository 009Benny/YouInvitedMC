package com.example.youinvited

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.youinvited.models.UserClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import kotlin.collections.HashMap

class LoginActivity : AppCompatActivity() {
    var progressBar:ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()
        setContentView(R.layout.activity_login)
        title = ""
        button_register.setOnClickListener { this.btnRegisterAction() }
        button_login.setOnClickListener { this.loginUser() }
    }

    fun loginUser(){
        if (textFieldEmail.text.isNotEmpty() && textFieldPass.text.isNotEmpty()) {
            this.progressBar?.visibility = View.VISIBLE
            FirebaseAuth.getInstance().signInWithEmailAndPassword(textFieldEmail.text.toString(),textFieldPass.text.toString()).addOnCompleteListener {
                this.progressBar?.visibility = View.INVISIBLE
                if (it.isSuccessful){
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user?.isEmailVerified == false){
                        Toast.makeText(this, "Su cuenta no ha sido verificada, favor de revisar su correo eléctronico", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, "Se inició sesión correctamente", Toast.LENGTH_LONG).show()
                        user?.uid?.let { uid -> this.getUserData(uid) }
                    }
                } else {
                    Toast.makeText(this, "Error al iniciar sesion", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    fun btnRegisterAction(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun showHome(email: String, admin:Boolean){
        val intent = Intent(this, PrincipalActivity::class.java).apply {
            putExtra("email", email)
            putExtra("admin", admin)
        }
        startActivity(intent)
        finish()
    }

    fun getUserData(uid:String){
        val database = FirebaseDatabase.getInstance()
        database.setPersistenceEnabled(true)
        val ref = database.getReference("Users")
        ref.child(uid).get().addOnSuccessListener {
            val user= it.getValue() as? UserClass
            if (user != null){
                this.showHome(user.email, user.admin)
            }
            val map = it.getValue() as? HashMap<String, Any>
            if (map != null){
                this.showHome(map["email"] as? String ?: "", map["admin"] as? Boolean ?: false)
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error al cargar usuario", Toast.LENGTH_SHORT).show()
        }
    }

    fun setLocale(language:String){
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        val editor = getSharedPreferences("Settings", Activity.MODE_PRIVATE).edit()
        editor.putString("My_Lang", language)
        editor.apply()
    }

    fun loadLocate(){
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        var language: String? = sharedPreferences.getString("My_Lang", "")
        if (language != null){
            setLocale(language!!)
        }
    }

}