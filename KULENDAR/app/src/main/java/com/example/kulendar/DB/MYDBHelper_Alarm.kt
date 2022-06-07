package com.example.kulendar.DB

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MYDBHelper_Alarm(var context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object{
        val DB_NAME = "alarm.db"
        val DB_VERSION = 1
        val TABLE_NAME = "Alarm"
        val Notification_ID = "Notification_ID"
        val Date = "Date"
        val Title = "Title"
        val Repeat = "Repeat"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "create table if not exists ${MYDBHelper_Alarm.TABLE_NAME}("+
                "$Notification_ID integer primary key,"+
                "$Date text,"+
                "$Title text,"+
                "$Repeat integer);"
        db!!.execSQL(create_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table = "drop table if exists ${MYDBHelper_Alarm.TABLE_NAME};"
        db!!.execSQL(drop_table)
        onCreate(db)
    }

    @SuppressLint("Range")
    private fun getAllRecord() {
        val strsql = "select * from ${MYDBHelper_Alarm.TABLE_NAME};"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        //showRecord(cursor)

        while (cursor.moveToNext()) {
            val nid = cursor.getInt(cursor.getColumnIndex("Notification_ID"))
            val date = cursor.getString(cursor.getColumnIndex("Date"))
            val title = cursor.getString(cursor.getColumnIndex("Title"))
            val repeat = cursor.getInt(cursor.getColumnIndex("Repeat"))
        }

        cursor.close()
        db.close()
    }

    fun insertAlarm(alarm: Alarm) {
        val values = ContentValues()
        values.put("Notification_ID",alarm.notification_ID)
        values.put("Date",alarm.date)
        values.put("Title",alarm.title)
        values.put("Repeat",alarm.repeatOnOff)

        val wd = writableDatabase
        wd.insert("Alarm",null,values)
        wd.close()
    }

    fun updateAlarm(alarm: Alarm) {
        val values = ContentValues()
        values.put("Notification_ID",alarm.notification_ID)
        values.put("Date",alarm.date)
        values.put("Title",alarm.title)
        values.put("Repeat",alarm.repeatOnOff)

        val wd = writableDatabase
        wd.update("Alarm",values,"id=${alarm.notification_ID}",null)
        wd.close()
    }

    fun deleteAlarm(alarm: Alarm) {
        val delete = "delete from Alarm where id = ${alarm.notification_ID}"
    }


}