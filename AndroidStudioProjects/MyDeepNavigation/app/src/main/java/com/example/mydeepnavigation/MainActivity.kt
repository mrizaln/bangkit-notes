package com.example.mydeepnavigation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.mydeepnavigation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOpenDetail.setOnClickListener(this)

        showNotification(
            this@MainActivity,
            getString(R.string.notification_title),
            getString(R.string.notification_message),
            110
        )
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnOpenDetail -> {
                val detailIntent = Intent(this@MainActivity, DetailActivity::class.java)
                    .putExtra(DetailActivity.Extra.TITLE.string, getString(R.string.detail_title))
                    .putExtra(DetailActivity.Extra.MESSAGE.string, getString(R.string.detail_message))
                startActivity(detailIntent)
            }
        }
    }

    private fun showNotification(
        context: Context,
        title: String,
        message: String,
        notificationId: Int,
    ) {
        val CHANNEL_ID = "channel_1"
        val CHANNEL_NAME = "Navigation channel"

        val notificationDetailIntent = Intent(this, DetailActivity::class.java)
            .putExtra(DetailActivity.Extra.TITLE.string, title)
            .putExtra(DetailActivity.Extra.MESSAGE.string, message)

        val pendingIntent = TaskStackBuilder.create(this).run {
            addParentStack(DetailActivity::class.java)
            addNextIntent(notificationDetailIntent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                getPendingIntent(
                    110,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            else
                getPendingIntent(110, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_email_black_24dp)
            .setColor(ContextCompat.getColor(context, android.R.color.black))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            notificationBuilder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = notificationBuilder.build()
        notificationManager.notify(notificationId, notification)
    }
}