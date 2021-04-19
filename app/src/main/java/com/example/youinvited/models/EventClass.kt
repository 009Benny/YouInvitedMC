package com.example.youinvited.models

import java.util.*

data class EventClass(
    val event_id:String = "",
    val user_id:String = "",
    val name:String = "",
    val address:String = "",
    val countInvited:Int = 0,
    val modificationDate: Date? = null,
    val eventDate: Date? = null
    ){

}

//data class UserClass (val uid:String = "", val username:String = "", val email:String = "", val address:String = "", val phone:String = "", val admin:Boolean = false){
//
//}