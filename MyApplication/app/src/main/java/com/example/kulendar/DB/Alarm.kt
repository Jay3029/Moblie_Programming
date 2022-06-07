package com.example.kulendar.DB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlarmTable")
data class Alarm (
    @PrimaryKey(autoGenerate = true) var notification_ID:Int,
    var date:String,
    var title:String,
    var repeatOnOff:Int
)