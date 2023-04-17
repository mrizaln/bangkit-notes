package com.example.mymediaplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var service: Messenger? = null
    private lateinit var boundServiceIntent: Intent
    private var serviceBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
            serviceBound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            this@MainActivity.service = Messenger(service)
            serviceBound = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPlay = findViewById<Button>(R.id.btn_play).apply {
            setOnClickListener {
                try { service?.send(Message.obtain(null, MediaService.Action.PLAY.ordinal, 0, 0)) }
                catch (e: RemoteException ) { e.printStackTrace() }
            }
        }

        val btnStop = findViewById<Button>(R.id.btn_stop).apply {
            setOnClickListener {
                try { service?.send(Message.obtain(null, MediaService.Action.STOP.ordinal, 0, 0)) }
                catch (e: RemoteException ) { e.printStackTrace() }
            }
        }

        boundServiceIntent = Intent(this@MainActivity, MediaService::class.java)
        startService(boundServiceIntent)
        bindService(boundServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        unbindService(serviceConnection)
        stopService(boundServiceIntent)
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}