package com.example.kulendar.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import com.example.kulendar.DB.Alarm
import com.example.kulendar.DB.MYDBHelper_Alarm
import com.example.kulendar.R
import com.example.kulendar.databinding.FragmentAlarmTab1Binding
import java.sql.Array
import java.util.*
import kotlin.collections.ArrayList


class AlarmTab1Fragment : Fragment() {

    lateinit var binding : FragmentAlarmTab1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        init()

        binding = FragmentAlarmTab1Binding.inflate(inflater, container, false)
        return binding.root
    }

    fun init() {
        var saveAlarmBtn = binding.saveAlarmBtn
        saveAlarmBtn.setOnClickListener {

            // datepicker에서 설정한 날짜 가져오기
            var datePicker = binding.datePicker1
            var year = datePicker.year
            var month = datePicker.month
            var day = datePicker.dayOfMonth

            // 선택한 날짜 합치기 (시간은 9 PM)
            var selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, day, 21, 0, 0)

            // 현재 날짜
            var currentDate = Calendar.getInstance()

            // 두 날짜 차이 ,  24 * 60 * 60 = 86400000 으로 나누면 몇일인지만 나옴
            var calcuDate =
                ((currentDate.timeInMillis - selectedDate.timeInMillis) / 86400000).toInt() + 1

            var date = year.toString() + "." + (month + 1).toString() + "." + day.toString() + "."
            var title = binding.alarmTitle.toString()

            // alarm 등록
            val alarManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

            activity?.let {
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

            }
            // alarm data 저장(repeat은 기본적으로 false로 저장)
            val alarmdata = Alarm(currentDate.timeInMillis,date,title,0)
            val mydbhelperAlarm = MYDBHelper_Alarm(requireContext())
            mydbhelperAlarm.insertAlarm(alarmdata)
        }
    }
}