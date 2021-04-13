package com.example.youinvited.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.youinvited.models.UserClass

class ProfileViewModel : ViewModel() {

    var user_Data:MutableLiveData<UserClass> = MutableLiveData<UserClass>()

}