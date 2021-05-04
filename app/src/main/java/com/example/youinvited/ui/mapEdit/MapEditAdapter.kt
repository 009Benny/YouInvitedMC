package com.example.youinvited.ui.mapEdit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.youinvited.R
import com.example.youinvited.models.InvitedClass
import kotlinx.android.synthetic.main.item_invited.view.*
import kotlin.collections.ArrayList

class MapEditAdapter(
    private var inviteds:ArrayList<InvitedClass>,
    private val listener: OnItemClickListener
): RecyclerView.Adapter<MapEditAdapter.InvitedHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvitedHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_invited, parent, false)
        return InvitedHolder(itemView)
    }

    override fun onBindViewHolder(holder: InvitedHolder, position: Int) {
        holder.renderCell(this.inviteds[position])
    }

    override fun getItemCount() = this.inviteds.size

    fun setData(persons:ArrayList<InvitedClass>){
        this.inviteds = persons
        this.notifyDataSetChanged()
    }

    inner class InvitedHolder(val item: View): RecyclerView.ViewHolder(item),
        View.OnClickListener{

        init {
            item.setOnClickListener(this)
        }

        fun renderCell(invited: InvitedClass){
            item.textTitle.text = invited.public_name
            if (invited.x != null && invited.y != null){
                val x:Double = invited.x ?: 0.0
                val y:Double = invited.y ?: 0.0
                item.textCord.text = "Coordenadas:\nX: $x, Y: $y"
            }else{
                item.textCord.text = "Coordenadas:\nX: - , Y: -"
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onSelectInvited(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onSelectInvited(position: Int)
    }

}