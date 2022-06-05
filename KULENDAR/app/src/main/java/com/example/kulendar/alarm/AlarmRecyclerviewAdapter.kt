package com.example.kulendar.alarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kulendar.DB.Alarm
import com.example.kulendar.R
import com.example.kulendar.databinding.AlarmitemListBinding
import java.util.*
import kotlin.collections.ArrayList

class AlarmRecyclerviewAdapter (val items:ArrayList<Alarm>)
    : RecyclerView.Adapter<AlarmRecyclerviewAdapter.AlarmViewHolder>() {

    inner class AlarmViewHolder(var binding: AlarmitemListBinding): RecyclerView.ViewHolder(binding.root){
        fun setAlarm(alarm: Alarm) {
            binding.alarmDate.text = alarm.date
            binding.alarmInfotext.text = alarm.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : AlarmRecyclerviewAdapter.AlarmViewHolder {
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


}