package com.example.mybroadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mybroadcastreceiver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val ACTION_DOWNLOAD_STATUS = "download_status"
        private const val SMS_REQUEST_CODE = 101
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var downloadReceiver: BroadcastReceiver

    var requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        val toastMessage = "Sms receiver permission " + if (isGranted) "diterima" else "ditolak"
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnPermission.setOnClickListener(this@MainActivity)
            btnDownload.setOnClickListener(this@MainActivity)
        }

        downloadReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.d(DownloadService.TAG, "Download selesai")
                Toast.makeText(context, "Download selesai", Toast.LENGTH_SHORT).show()
            }
        }

        val downloadIntentFilter = IntentFilter(ACTION_DOWNLOAD_STATUS)
        registerReceiver(downloadReceiver, downloadIntentFilter)
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnPermission -> requestPermissionLauncher.launch(android.Manifest.permission.RECEIVE_SMS)
            binding.btnDownload -> {
                val downloadServiceIntent = Intent(this, DownloadService::class.java)
                startService(downloadServiceIntent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(downloadReceiver)
    }
}