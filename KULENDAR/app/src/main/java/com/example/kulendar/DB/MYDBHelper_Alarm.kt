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
        val TABLE_NAME = "alarm"
        val Notification_Id = 1000
        val Date = "date"
        val Title = "title"
        val Repeat = 0

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "create table if not exists ${MYDBHelper_Alarm.TABLE_NAME}("+
                "$Notification_Id integer primary key,"+
                "$Date text,"+
                "$Title text,"+
                "$Repeat integer);"
        db!!.execSQL(create_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table = "drop table if exists ${MYDBHelper_Schedule.TABLE_NAME};"
        db!!.execSQL(drop_table)
        onCreate(db)
    }

    private fun getAllRecord() {
        val strsql = "select * from ${MYDBHelper_Subject.TABLE_NAME};"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        //showRecord(cursor)
        cursor.close()
        db.close()
    }

    fun insertAlarm(alarm: Alarm) {
        val values = ContentValues()
        values.put("notification_id",alarm.notification_ID)
        values.put("date",alarm.date)
        values.put("title",alarm.title)
        values.put("repeat",alarm.repeatOnOff)

        val wd = writableDatabase
        wd.insert("alarm",null,values)
        wd.close()
    }


    @SuppressLint("Range")
    fun selectAlarm():ArrayList<Alarm> {
        val alarmList = ArrayList<Alarm>()
        // 전체 조회
        val selectAll = "select * from alarm"
        val rd = readableDatabase
        val cursor = rd.rawQuery(selectAll,null)

        // list에 데이터 담기
        while (cursor.moveToNext()){
            val nid = cursor.getLong(cursor.getColumnIndex("notification_ID"))
            val date = cursor.getString(cursor.getColumnIndex("date"))
            val title = cursor.getString(cursor.getColumnIndex("title"))
            val repeat = cursor.getInt(cursor.getColumnIndex("repeatOnOff"))

            alarmList.add(Alarm(nid,date,title,repeat))
        }

        cursor.close()
        rd.close()

        return alarmList
    }

    fun updateAlarm(alarm: Alarm) {
        val values = ContentValues()
        values.put("notification_id",alarm.notification_ID)
        values.put("date",alarm.date)
        values.put("title",alarm.title)
        values.put("repeat",alarm.repeatOnOff)

        val wd = writableDatabase
        wd.update("alarm",values,"id=${alarm.notification_ID}",null)
        wd.close()
    }

    fun deleteAlarm(alarm: Alarm) {
        val delete = "delete from Alarm where id = ${alarm.notification_ID}"
    }

}