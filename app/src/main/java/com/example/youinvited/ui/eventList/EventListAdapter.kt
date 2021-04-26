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

class EventListAdapter(
    private var events:ArrayList<EventClass>,
    private val listener: OnItemClickListener
):RecyclerView.Adapter<EventListAdapter.EventHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.renderCell(this.events[position])
    }

    override fun getItemCount() = this.events.size

    fun setData(events:ArrayList<EventClass>){
        this.events = events
        this.notifyDataSetChanged()
    }

    inner class EventHolder(val item: View):RecyclerView.ViewHolder(item),
        View.OnClickListener{

        init {
            item.setOnClickListener(this)
        }

        fun renderCell(event:EventClass){
            val date = event.eventDate ?: Date()
            val string = SimpleDateFormat("dd - MM - yyyy").format(date)
            val address = event.address
            val count = event.countInvited
            item.textEventDate.text = "Fecha: $string"
            item.textTitleAddress.text = "Dirección: $address"
            item.textTitleEvent.text = event.name
            item.textInvitedCount.text = "No. Invitados: ${count}"
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.showEvent(position)
            }
        }
    }

    interface OnItemClickListener{
        fun showEvent(position: Int)
    }

}