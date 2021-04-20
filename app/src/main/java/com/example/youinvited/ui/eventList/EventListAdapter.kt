package com.example.youinvited.ui.eventList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.youinvited.R
import com.example.youinvited.models.EventClass
import kotlinx.android.synthetic.main.item_event.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventListAdapter(var events:ArrayList<EventClass>):RecyclerView.Adapter<EventListAdapter.EventHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EventHolder(layoutInflater.inflate(R.layout.item_event, parent, false))
    }

    override fun getItemCount(): Int {
        return this.events.size
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.renderCell(this.events[position])
    }

    fun setData(events:ArrayList<EventClass>){
        this.events = events
        this.notifyDataSetChanged()
    }

    class EventHolder(val view: View):RecyclerView.ViewHolder(view){

        fun renderCell(event:EventClass){
            val date = event.eventDate ?: Date()
            val string = SimpleDateFormat("dd - MM - yyyy").format(date)
            val address = event.address
            val count = event.countInvited
            view.textEventDate.text = "Fecha: $string"
            view.textTitleAddress.text = "Dirección: $address"
            view.textTitleEvent.text = event.name
            view.textInvitedCount.text = "No. Invitados: ${count}"
        }

    }
}