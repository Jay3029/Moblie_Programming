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
import android.widget.Toast
import com.example.kulendar.DB.Alarm
import com.example.kulendar.DB.AlarmDatabase
import com.example.kulendar.R
import com.example.kulendar.databinding.FragmentAlarmTab1Binding
import com.sun.mail.auth.OAuth2SaslClientFactory.init
import java.util.*


class AlarmTab1Fragment : Fragment() {

    lateinit var binding: FragmentAlarmTab1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmTab1Binding.inflate(inflater, container, false)

        return binding.root
    }

}