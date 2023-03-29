package com.example.myalarmmanager

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val TIME_FORMAT = "HH:mm"
    }

    enum class Extra(val key: String) {
        MESSAGE("message"),
        TYPE("type"),
    }

    enum class Type(val key: String, val id: Int) {
        ONE_TIME("OneTimeAlarm", 100),
        REPEATING("RepeatingAlarm", 101)
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val type = intent.getStringExtra(Extra.TYPE.key) ?: ""
        val message = intent.getStringExtra(Extra.MESSAGE.key) ?: ""

        // @formatter:off
        val (title, notificationId) = when (type) {
            Type.ONE_TIME.key  -> Pair(Type.ONE_TIME.key, Type.ONE_TIME.id)
            Type.REPEATING.key -> Pair(Type.REPEATING.key, Type.REPEATING.id)
            else               -> Pair("", 0)
        }
        // @formatter:on
        showToast(context, title, message)

        showAlarmNotification(context, title, message, notificationId)
    }

    private fun showToast(context: Context, title: String, message: String) {
        Toast.makeText(context, "$title: $message", Toast.LENGTH_LONG).show()
    }

    fun setOneTimeAlarm(context: Context, type: Type, date: String, time: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
            .putExtra(Extra.MESSAGE.key, message.ifBlank { "test" })
            .putExtra(Extra.TYPE.key, type.key)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Type.ONE_TIME.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        if (isDateInvalid(date, DATE_FORMAT) || isDateInvalid(time, TIME_FORMAT)) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 1000,
                pendingIntent
            )
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            Toast.makeText(
                context,
                "One time alarm set up (${dateFormat.format(Date(System.currentTimeMillis()))})",
                Toast.LENGTH_SHORT
            ).show()
            return
        }


        Log.d("ONE TIME", "$date $time")
        val (year, month, day) = date.split("-").toTypedArray().map { Integer.parseInt(it) }
        val (hour, minute) = time.split(":").toTypedArray().map { Integer.parseInt(it) }

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(
                Calendar.MONTH,
                month - 1
            )    // https://stackoverflow.com/questions/344380/why-is-january-month-0-in-java-calendar
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val deltaTime = (calendar.timeInMillis - System.currentTimeMillis()) / 1000
        Toast.makeText(
            context,
            "One time alarm set up (${dateFormat.format(calendar.time)}) [${deltaTime}s]",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun setRepeatingAlarm(context: Context, type: Type, time: String, message: String) {
        if (isDateInvalid(time, TIME_FORMAT))
            return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
            .putExtra(Extra.MESSAGE.key, message)
            .putExtra(Extra.TYPE.key, type.key)

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val (hour, minute) = timeArray.map { Integer.parseInt(it) }

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Type.REPEATING.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val deltaTime = (calendar.timeInMillis - System.currentTimeMillis()) / 1000
        Toast.makeText(context, "Repeating alarm set up (${dateFormat.format(calendar.time)}) [${deltaTime}s]\",", Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context, type: Type) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = type.id
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Repeating alarm dibatalkan", Toast.LENGTH_SHORT).show()
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }

    private fun showAlarmNotification(
        context: Context,
        title: String,
        message: String,
        notificationId: Int,
    ) {
        val channelId = "channel_1"
        val channelName = "AlarmManager channel"

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_access_time_black)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(channelId)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager.notify(notificationId, notification)
    }
}