package com.example.kulendar.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.example.kulendar.DB.Alarm
import com.example.kulendar.DB.MYDBHelper_Alarm
import com.example.kulendar.R
import java.util.*

class AlarmTab1Fragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_alarm_tab1, container, false)

        init(view)
        return view
    }

    private fun init(view:View) {
        val mydbhelperAlarm:MYDBHelper_Alarm
        val mydbAlarm:SQLiteDatabase
        mydbhelperAlarm = MYDBHelper_Alarm(requireContext())
        mydbAlarm = mydbhelperAlarm.writableDatabase

        var saveAlarmBtn = view.findViewById<Button>(R.id.saveAlarmBtn)
        saveAlarmBtn.setOnClickListener {

            // datepicker에서 설정한 날짜 가져오기
            var datePicker = view.findViewById<DatePicker>(R.id.datePicker1)
            var year = datePicker.year
            var month = datePicker.month
            var day = datePicker.dayOfMonth

            // 선택한 날짜 합치기 (시간은 9 PM)
            var selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, day, 21, 0, 0)

            // 현재 날짜
            var currentDate = Calendar.getInstance()

            var date = year.toString() + "." + (month + 1).toString() + "." + day.toString()
            var title = view.findViewById<EditText>(R.id.alarm_title).toString()

            // alarm 등록
            val alarManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                currentDate.timeInMillis.toInt(),
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            intent.putExtra("nid",currentDate.timeInMillis)
            intent.putExtra("date",date)
            intent.putExtra("title",title)

            alarManager.set(
                AlarmManager.RTC_WAKEUP,
                (selectedDate.timeInMillis/86400000 -1).toLong(), // 하루 전날 21시에 울리게 설정
                pendingIntent
            )


            // alarm data 저장(repeat은 기본적으로 false로 저장)
            val alarmdata = Alarm(currentDate.timeInMillis.toInt(),date,title,0)
            mydbhelperAlarm.insertAlarm(alarmdata)

            Toast.makeText(requireContext(),title+" 알림이 "+date+" 에 등록되었습니다.",Toast.LENGTH_SHORT)
        }
    }
}