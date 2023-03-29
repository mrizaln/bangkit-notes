package com.example.mybroadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log

class SmsReceiver : BroadcastReceiver() {
    companion object {
        private val TAG = this::class.java.simpleName
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val bundle = intent.extras

        try {
            if (bundle == null)
                return

            // Bundle dengan key "pdus" sudah merupakan standar yang digunakan oleh system
            val pdusObj = bundle.get("pdus") as Array<*>
            pdusObj.forEach {  pdus ->
                val currentmessage = getIncomingMessage(pdus as Any, bundle)
                val senderNumber = currentmessage.displayOriginatingAddress
                val message = currentmessage.displayMessageBody
                Log.d(TAG, "senderNum: $senderNumber; message: $message")

                val showSmsIntent = Intent(context, SmsReceiverActivity::class.java)
                showSmsIntent.apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra(SmsReceiverActivity.Extra.SMS_NUMBER.string, senderNumber)
                    putExtra(SmsReceiverActivity.Extra.SMS_MESSAGE.string, message)
                }
                context.startActivity(showSmsIntent)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Exception $TAG: $e")
        }
    }

    private fun getIncomingMessage(aObject: Any, bundle: Bundle): SmsMessage {
        return if (Build.VERSION.SDK_INT >= 23) {
            val  format = bundle.getString("format")
            SmsMessage.createFromPdu(aObject as ByteArray, format)
        } else {
            SmsMessage.createFromPdu(aObject as ByteArray)
        }
    }
}