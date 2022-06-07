package com.example.kulendar.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.kulendar.DB.Alarm
import com.example.kulendar.DB.AlarmDatabase
import com.example.kulendar.R
import com.example.kulendar.databinding.FragmentAlarmTab1Binding
import com.example.kulendar.databinding.FragmentAlarmTab2Binding
import com.sun.mail.auth.OAuth2SaslClientFactory.init
import java.util.*

class AlarmTab2Fragment : Fragment() {

    lateinit var binding: FragmentAlarmTab2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmTab2Binding.inflate(inflater, container, false)

        return binding.root
    }
}


