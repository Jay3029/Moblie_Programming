package com.example.kulendar.alarm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kulendar.DB.Alarm
import com.example.kulendar.DB.MYDBHelper_Alarm
import com.example.kulendar.R
import com.example.kulendar.databinding.AlarmitemListBinding
import com.example.kulendar.databinding.FragmentAlarmTab2Binding

class AlarmTab2Fragment : Fragment() {

    lateinit var binding: FragmentAlarmTab2Binding
    lateinit var rpbinding: AlarmitemListBinding

    val alarmData = ArrayList<Alarm>()
    val alarmRecyclerView = binding.alarmRecyclerView

    val mydbhelperAlarm = MYDBHelper_Alarm(requireContext())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        init()

        binding = FragmentAlarmTab2Binding.inflate(inflater,container,false)
        return binding.root
    }



    private fun init() {
        var alarmlistAdapter = AlarmRecyclerviewAdapter(alarmData)
        alarmRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        alarmlistAdapter.items.addAll(mydbhelperAlarm.selectAlarm())

        alarmRecyclerView.adapter = alarmlistAdapter






        var repeatOnOffBtn = rpbinding.repeatOnOffBtn
        repeatOnOffBtn.setOnClickListener {
            /* onoff이벤트
            if() {

            } else {

            }
            */
        }

        // 스와이프 삭제 기능


    }
}