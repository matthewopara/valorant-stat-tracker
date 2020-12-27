package com.example.valorantstattracker.objects

import java.text.SimpleDateFormat
import java.util.Date

object TimeCalculator {

    fun getDate(timeInMilli: Long): String {
        return SimpleDateFormat.getDateInstance().format(Date(timeInMilli))
    }

    fun getTime(timeInMilli: Long): String {
        var time = SimpleDateFormat.getTimeInstance().format(Date(timeInMilli))
        time = time.removeRange(4..6) // removes the seconds from the string
        return time
    }
}