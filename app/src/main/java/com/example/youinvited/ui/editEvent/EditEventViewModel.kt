package com.example.youinvited.ui.editEvent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.youinvited.models.EventClass
import com.example.youinvited.models.UserClass

class EditEventViewModel : ViewModel() {

    var event_Data: MutableLiveData<EventClass> = MutableLiveData<EventClass>()

}