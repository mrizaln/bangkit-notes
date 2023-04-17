package com.example.mymediaplayer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import java.io.IOException
import java.lang.ref.WeakReference

class MediaService : Service(), MediaPlayerCallback {
    private val messenger = Messenger(IncomingHandler(this))
    private var mediaPlayerIsReady = false

    private val mediaPlayer by lazy {
        val attribute = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

        MediaPlayer().apply {
            setAudioAttributes(attribute)
            // thanks to [8bit] ImCa for this absolute banger! (https://www.youtube.com/watch?v=WiosH1_ldxg)
            val afd = applicationContext.resources.openRawResourceFd(R.raw.unwelcome_school)
            try {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            setOnPreparedListener {
                mediaPlayerIsReady = true
                start()
                showNotification()
            }
            setOnCompletionListener {
                stopNotification()
            }
            setOnErrorListener { _, _, _ -> false }
            Log.d(TAG, "MediaPlayer initialized")
        }
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind: ")
        return messenger.binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return flags
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: ")
        mediaPlayer.stop()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        mediaPlayer.stop()
        stopSelf()
        super.onDestroy()
    }

    override fun onPlay() {
        Log.d(TAG, "onPlay called")
        if (!mediaPlayerIsReady) {
            mediaPlayer.prepareAsync()
        } else {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            } else {
                mediaPlayer.start()
                showNotification()
            }
        }
    }

    override fun onStop() {
        Log.d(TAG, "onStop called")
        if (mediaPlayer.isPlaying || mediaPlayerIsReady) {
            mediaPlayer.stop()
            mediaPlayerIsReady = false
            stopNotification()
        }
    }

    private fun showNotification() {
        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        }

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )
        else PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("playing music")
            .setContentText("music is playing")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(pendingIntent)
            .setTicker("test_ticker").build()

        createChannel(CHANNEL_ID)
        startForeground(ONGOING_NOTIFICATION_ID, notification)
    }

    private fun createChannel(channelId: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Battery",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setShowBadge(false)
                setSound(null, null)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun stopNotification() {
        stopForeground(true)
    }

    enum class Action {
        PLAY, STOP
    }

    internal class IncomingHandler(
        playerCallback: MediaPlayerCallback,
    ) : Handler(Looper.getMainLooper()) {
        private val mediaPlayerCallbackWeakReference = WeakReference(playerCallback)

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                Action.PLAY.ordinal -> mediaPlayerCallbackWeakReference.get()?.onPlay()
                Action.STOP.ordinal -> mediaPlayerCallbackWeakReference.get()?.onStop()
                else -> super.handleMessage(msg)
            }
        }
    }

    companion object {
        private val TAG = MediaService::class.java.simpleName
        const val CHANNEL_ID = "channel_test"
        const val ONGOING_NOTIFICATION_ID = 1
    }
}