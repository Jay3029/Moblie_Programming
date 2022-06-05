package com.example.kulendar.alarm

data class AlarmDBModel(val date:String, val title:String, val repeatOnOff:Boolean) {

    fun makeDataForDB():String {
        return "date: $date / title: $title / repeat:$repeatOnOff"
    }

    val onoffText:String
        get() {
            return if(repeatOnOff) "중요알리미" else "기본알리미"
    }

}
