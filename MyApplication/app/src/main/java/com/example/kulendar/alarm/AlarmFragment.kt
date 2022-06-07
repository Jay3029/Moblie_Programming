package com.example.kulendar.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kulendar.DB.Alarm
import com.example.kulendar.DB.AlarmDatabase
import com.example.kulendar.R
import com.example.kulendar.databinding.AlarmitemListBinding
import com.example.kulendar.databinding.FragmentAlarmBinding
import com.example.kulendar.databinding.FragmentAlarmTab1Binding
import com.example.kulendar.databinding.FragmentAlarmTab2Binding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*


class AlarmFragment : Fragment() {

    private var TAG = "AlarmFragment"

    lateinit var binding: FragmentAlarmBinding
    lateinit var tab1Binding: FragmentAlarmTab1Binding
    lateinit var tab2Binding: FragmentAlarmTab2Binding

    var alarmDB: AlarmDatabase? = null
    var alarmList = mutableListOf<Alarm>()
    var adapter = AlarmListAdapter(alarmList)

    //탭타이틀 설정
    private val tabTitleArray = arrayListOf(
        "알림 추가",
        "주요 알리미"
    )
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAlarmBinding.inflate(inflater, container, false)


        //탭레이아웃과 뷰페이저 연결
        val alarmAdapter = AlarmVPAdapter(this)
        binding.alarmViewPager.adapter = alarmAdapter
        TabLayoutMediator(binding.alarmTabLayout, binding.alarmViewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()


        init()

        return binding.root //setContentView

    }

    fun init() {

        alarmDB = AlarmDatabase.getInstance(requireContext())

        val savedAlarms = alarmDB!!.alarmDao().getAll()
        if (savedAlarms.isNotEmpty()) {
            alarmList.addAll(savedAlarms)
        }

        adapter.setItemClickListener(object: AlarmListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val alarm = alarmList[position]

                if(alarm.repeatOnOff == 0) {
                    alarm.repeatOnOff = alarm.repeatOnOff + 1

                    // 날짜 받아서 연, 월, 일 별로 분할
                    var dateArr = alarm.date.split(".")
                    var selectedDate = Calendar.getInstance()
                    selectedDate.set(dateArr[0].toInt(), dateArr[1].toInt() - 1, dateArr[2].toInt(), 21,0,0)
                    var currentDate = Calendar.getInstance()

                    // 버튼 클릭 시 시간과 알림 설정된 시간과의 차이(단위: 일)
                    var calcuDate = ((currentDate.timeInMillis - selectedDate.timeInMillis)/86400000).toInt() + 1

                    val intent = Intent(context, AlarmReceiver::class.java)
                    val alarmManager:AlarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    var currentHour = currentDate.get(Calendar.HOUR_OF_DAY)

                    if(currentHour < 21) {

                        for (i:Int in calcuDate..0 step(-2)) {
                            val pendingIntent = PendingIntent.getBroadcast(
                                requireContext(),
                                currentDate.timeInMillis.toInt(), // 해당 코드로 pendingIntent 등록
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                            )

                            alarmManager.set(
                                AlarmManager.RTC_WAKEUP,
                                (selectedDate.timeInMillis/86400000 - i + 1).toLong(), // 하루 전날 21시에 울리게 설정
                                pendingIntent)
                        }

                    } else {

                        for (i:Int in calcuDate..0 step(-2)) {

                            val pendingIntent = PendingIntent.getBroadcast(
                                requireContext(),
                                currentDate.timeInMillis.toInt(), // 해당 코드로 pendingIntent 등록
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                            )

                            alarmManager.set(
                                AlarmManager.RTC_WAKEUP,
                                (selectedDate.timeInMillis/86400000 - 1 - (i-1)).toLong(), // 하루 전날 21시에 울리게 설정
                                pendingIntent)
                        }
                    }

                    alarmDB?.alarmDao()?.update(alarm) // DB에 갱신
                    adapter.notifyDataSetChanged() // list 갱신

                    Toast.makeText(context, "알림 반복", Toast.LENGTH_SHORT).show()


                } else {
                    alarm.repeatOnOff = alarm.repeatOnOff - 1

                    alarmDB?.alarmDao()?.update(alarm) // DB에 갱신
                    adapter.notifyDataSetChanged() // list 갱신

                    Toast.makeText(context, "취소 안됨", Toast.LENGTH_SHORT).show()
                }
            }
        })

        // 알림 이동, 삭제
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT){
            override fun onMove (p0: RecyclerView,
                                 p1: RecyclerView.ViewHolder,
                                 p2: RecyclerView.ViewHolder): Boolean {
                adapter.moveItem(p1.adapterPosition, p2.adapterPosition)
                return true
            }
            override fun onSwiped (viewHolder : RecyclerView.ViewHolder,
                                   direction : Int){
                val alarm = alarmList[viewHolder.adapterPosition]
                adapter.removeItem(viewHolder.adapterPosition)
                val pendingIntent = PendingIntent.getBroadcast(
                    requireContext(),
                    alarm.notification_ID, // 이 값의 pendingIntent를 취소
                    Intent(requireContext(), AlarmReceiver::class.java),
                    PendingIntent.FLAG_NO_CREATE
                )
                pendingIntent?.cancel()

                alarmDB?.alarmDao()?.deleteAlarm(alarm = alarm) // DB에서 삭제
                alarmList.removeAt(viewHolder.adapterPosition) // list에서 삭제
                adapter.notifyDataSetChanged() // list 갱신

                Log.d(TAG, "remove item($viewHolder.adapterPosition), title:${alarm.title}")
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(tab2Binding.alarmRecyclerView)

        tab2Binding.alarmRecyclerView.adapter = adapter

        var saveAlarmBtn = tab1Binding.saveAlarmBtn
        saveAlarmBtn.setOnClickListener {
            saveAlarm()
        }
    }

    fun saveAlarm() {
        // 날짜 가져오기
        var datePicker = tab1Binding.datePicker1
        var year = datePicker.year
        var month = datePicker.month
        var day = datePicker.dayOfMonth

        // 선택한 날짜 생성 (시간은 9 PM)
        var selectedDate = Calendar.getInstance()
        selectedDate.set(year, month, day, 21, 0, 0)

        // 현재 날짜 및 시간
        var currentDate = Calendar.getInstance()


        // 알림 등록
        val alarmManager: AlarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            currentDate.timeInMillis.toInt(), // pendingIntent Requestcode값을 현재시간으로 줘서 중복 안되게 설정
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            (selectedDate.timeInMillis/86400000 - 1),
            pendingIntent
        )

        // 알림 저장
        var date = year.toString()+"."+month.toString()+"."+day.toString()
        var title = tab1Binding.alarmTitle.text.toString()
        var alarm = Alarm(currentDate.timeInMillis.toInt(), date, title, 0)

        var alarmDB: AlarmDatabase? = null
        alarmDB?.alarmDao()?.insertAlarm(alarm) // DB에 추가
        alarmList.add(alarm) // list에 추가
        adapter.notifyDataSetChanged()

        Toast.makeText(context, "$title alarm set at $date", Toast.LENGTH_SHORT)
    }

}