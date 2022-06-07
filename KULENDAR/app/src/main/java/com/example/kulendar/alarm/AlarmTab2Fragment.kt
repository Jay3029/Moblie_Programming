package com.example.kulendar.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kulendar.DB.Alarm
import com.example.kulendar.DB.MYDBHelper_Alarm
import com.example.kulendar.R
import java.util.*
import kotlin.collections.ArrayList

class AlarmTab2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_alarm_tab2, container, false)

        init(view)
        getAllRecord()

        return view
    }

    private fun init(view:View) {

        val alarmData = ArrayList<Alarm>()
        val alarmRecyclerView = view.findViewById<RecyclerView>(R.id.alarm_RecyclerView)

        val mydbhelperAlarm = MYDBHelper_Alarm(requireContext())


        var alarmlistAdapter = AlarmRecyclerviewAdapter(alarmData)
        alarmRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        alarmlistAdapter.items.
        alarmRecyclerView.adapter = alarmlistAdapter

        var nid:Int = 0
        var date:String = ""
        var title:String = ""
        var onOff:Int = 0

        var repeatOnOffBtn = view.findViewById<Button>(R.id.repeatOnOffBtn)
        repeatOnOffBtn.setOnClickListener {

            alarmlistAdapter.itemClickListener = object:AlarmRecyclerviewAdapter.OnItemClickListener {
                override fun onClick(v: Alarm, position:Int) {
                    nid = alarmlistAdapter.items[position].notification_ID
                    date = alarmlistAdapter.items[position].date
                    title = alarmlistAdapter.items[position].title
                    onOff = alarmlistAdapter.items[position].repeatOnOff
                }
            }

            if(onOff == 0) {
                onOff = 1 - onOff

                var dateArr = date.split(".")

                val alarManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

                var selectedDate = Calendar.getInstance()
                selectedDate.set(dateArr[0].toInt(), dateArr[1].toInt(), dateArr[2].toInt(), 21,0,0)
                var currentDate = Calendar.getInstance()
                var calcuDate =
                    ((currentDate.timeInMillis - selectedDate.timeInMillis) / 86400000).toInt() + 1

                activity?.let {
                    val intent = Intent(context, AlarmReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(
                        requireContext(),
                        currentDate.timeInMillis.toInt(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    intent.putExtra("nid",currentDate.timeInMillis)
                    intent.putExtra("date",date)
                    intent.putExtra("title",title)


                    for (i:Int in calcuDate..0 step(-2)) {
                        alarManager.set(
                            AlarmManager.RTC_WAKEUP,
                            (selectedDate.timeInMillis/86400000 -1- (i-1)).toLong(), // 하루 전날 21시에 울리게 설정
                            pendingIntent)
                    }
                }
            } else {
                onOff = 1 - onOff
                Toast.makeText(requireContext(),"안타깝지만 알림 취소는 안됩니다.",Toast.LENGTH_SHORT)

            }
        }

        // 스와이프 삭제 기능

        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT){
            override fun onMove (p0: RecyclerView,
                                 p1: RecyclerView.ViewHolder,
                                 p2: RecyclerView.ViewHolder): Boolean {
                alarmlistAdapter.moveItem(p1.adapterPosition, p2.adapterPosition)
                return true
            }
            override fun onSwiped (viewHolder : RecyclerView.ViewHolder,
                                   direction : Int){
                alarmlistAdapter.removeItem(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(alarmRecyclerView)

    }

}