package com.example.mybroadcastreceiver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mybroadcastreceiver.databinding.ActivitySmsReceiverBinding

class SmsReceiverActivity : AppCompatActivity() {
    enum class Extra(val string: String) {
        SMS_NUMBER("extra_sms_number"),
        SMS_MESSAGE("extra_sms_message"),
    }

    private lateinit var binding: ActivitySmsReceiverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySmsReceiverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.incoming_message)

        val senderNumber = intent.getStringExtra(Extra.SMS_NUMBER.string) ?: ""
        val senderMessage = intent.getStringExtra(Extra.SMS_MESSAGE.string) ?: ""

        binding.apply {
            tvFrom.text = getString(R.string.from, senderNumber)
            tvMessage.text = senderMessage
            btnClose.setOnClickListener { finish() }
        }
    }
}