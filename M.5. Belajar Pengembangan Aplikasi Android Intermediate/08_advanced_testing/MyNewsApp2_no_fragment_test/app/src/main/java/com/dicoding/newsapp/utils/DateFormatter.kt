package com.dicoding.newsapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


object DateFormatter {
    // @RequiresApi(Build.VERSION_CODES.O)     // tidak dibutuhkan karena desugaring
    fun formatDate(dateString_ISO8601: String, targetTimeZone: String): String {
        val instant = Instant.parse(dateString_ISO8601)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")
            .withZone(ZoneId.of(targetTimeZone))
        return formatter.format(instant)
    }
}