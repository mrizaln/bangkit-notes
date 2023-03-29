package com.example.mysimplenotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {
    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "dicoding_channel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun sendNotification(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://dicoding.com"))
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, flag)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notifications_black_48)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_notifications_black_48))
            .setContentTitle(resources.getString(R.string.content_title))
            .setContentText(resources.getString(R.string.content_text))
            .setSubText(resources.getString(R.string.subtext))
            .setAutoCancel(true)

        // membuat notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_NAME

            notificationBuilder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = notificationBuilder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}