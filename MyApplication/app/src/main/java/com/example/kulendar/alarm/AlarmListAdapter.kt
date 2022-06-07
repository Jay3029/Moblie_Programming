package com.example.kulendar.alarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kulendar.DB.Alarm
import com.example.kulendar.R
import com.example.kulendar.alarm.AlarmListAdapter.*
import com.example.kulendar.databinding.AlarmitemListBinding

class AlarmListAdapter(private val alarmList : MutableList<Alarm>) : RecyclerView.Adapter<AlarmViewHolder>()  {

    inner class AlarmViewHolder(v:View) :RecyclerView.ViewHolder(v){
        var view:View = v

        fun bind(alarm:Alarm) {
            view.findViewById<TextView>(R.id.alarm_date).text = alarm.date
            view.findViewById<TextView>(R.id.alarm_infotext).text = alarm.title
            if(alarm.repeatOnOff == 0) {
                view.findViewById<ImageButton>(R.id.repeatOnOffBtn).setImageResource(R.drawable.star_off)
            } else {
                view.findViewById<ImageButton>(R.id.repeatOnOffBtn).setImageResource(R.drawable.star_on)
            }
        }
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.alarmitem_list, parent, false)
        return AlarmViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val item = alarmList[position]

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.apply {
            bind(item)
        }
    }

    //ClickListener
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun moveItem(oldPos:Int, newPos:Int){
        val item = alarmList[oldPos]
        alarmList.removeAt(oldPos)
        alarmList.add(newPos, item)
        notifyItemMoved(oldPos, newPos)
    }

    fun removeItem(pos:Int){
        alarmList.removeAt(pos)
        notifyItemRemoved(pos)
    }

}