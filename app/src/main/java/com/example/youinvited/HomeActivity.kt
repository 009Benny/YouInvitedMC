package com.example.youinvited

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*

enum class ProviderType{
    BASIC
}

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        val bundle = intent.extras
//        val email:String = bundle?.getString("email") ?: ""
//        textViewEmail.text = email.toString()
//        btn_logout.setOnClickListener { this.logout() }
    }

    fun logout(){
        FirebaseAuth.getInstance().signOut()
        onBackPressed()
    }

}