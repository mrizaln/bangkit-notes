package com.example.mynotesapp2.helper

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}