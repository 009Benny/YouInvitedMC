package com.example.youinvited.models
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserClass (val uid:String = "", val username:String = "", val email:String = "", val address:String = "", val phone:String = "", val admin:Boolean = false){

}