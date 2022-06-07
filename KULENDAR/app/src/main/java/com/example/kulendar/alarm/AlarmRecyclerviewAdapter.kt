package com.example.kulendar.alarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.kulendar.DB.Alarm
import com.example.kulendar.R
import com.example.kulendar.alarm.AlarmRecyclerviewAdapter.AlarmViewHolder
import com.example.kulendar.databinding.AlarmitemListBinding
import java.util.*
import kotlin.collections.ArrayList

class AlarmRecyclerviewAdapter (val items:ArrayList<Alarm>)
    : RecyclerView.Adapter<AlarmViewHolder>() {

    interface OnItemClickListener {
        fun onClick(v: Alarm, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class AlarmViewHolder(var binding: AlarmitemListBinding): RecyclerView.ViewHolder(binding.root){
        val onOffBtn = binding.repeatOnOffBtn
        init {
            onOffBtn.setOnClickListener {
                itemClickListener?.onClick(items[adapterPosition],adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : AlarmViewHolder {
        val binding = AlarmitemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.binding.alarmDate.text = items[position].date
        holder.binding.alarmInfotext.text = items[position].title
        if(items[position].repeatOnOff == 0){
            holder.binding.repeatOnOffBtn.setImageResource(R.drawable.star_off)
        } else {
            holder.binding.repeatOnOffBtn.setImageResource(R.drawable.star_on)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    fun moveItem(oldPos:Int, newPos:Int){
        val item = items[oldPos]
        items.removeAt(oldPos)
        items.add(newPos, item)
        notifyItemMoved(oldPos, newPos)
    }

    fun removeItem(pos:Int){
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }



}