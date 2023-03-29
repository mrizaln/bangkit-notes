package com.example.myalarmmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myalarmmanager.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {
    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_ONCE_TAG = "TimePickerOnce"
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnOnceTime.setOnClickListener(this@MainActivity)
            btnOnceDate.setOnClickListener(this@MainActivity)
            btnSetOnceAlarm.setOnClickListener(this@MainActivity)
            btnRepeatingTime.setOnClickListener(this@MainActivity)
            btnSetRepeatingAlarm.setOnClickListener(this@MainActivity)
            btnCancelRepeatingAlarm.setOnClickListener(this@MainActivity)
        }

        alarmReceiver = AlarmReceiver()
    }

    override fun onClick(view: View) {
        val b = binding
        when (view.id) {
            b.btnOnceDate.id -> {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
            }
            b.btnOnceTime.id -> {
                val timePickerFragmentOnce = TimePickerFragment()
                timePickerFragmentOnce.show(supportFragmentManager, TIME_PICKER_ONCE_TAG)
            }
            b.btnSetOnceAlarm.id -> {
                val onceDate = b.tvOnceDate.text.toString()
                val onceTime = b.tvOnceTime.text.toString()
                val onceMessage = b.edtOnceMessage.text.toString()

                alarmReceiver.setOneTimeAlarm(
                    this,
                    AlarmReceiver.Type.ONE_TIME,
                    onceDate,
                    onceTime,
                    onceMessage
                )
            }
            b.btnRepeatingTime.id -> {
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(supportFragmentManager, TIME_PICKER_REPEAT_TAG)
            }
            b.btnSetRepeatingAlarm.id -> {
                val repeatTime = binding.tvRepeatingTime.text.toString()
                val repeatMessage = binding.edtRepeatingMessage.text.toString()
                alarmReceiver.setRepeatingAlarm(
                    this,
                    AlarmReceiver.Type.REPEATING,
                    repeatTime,
                    repeatMessage
                )
            }
            b.btnCancelRepeatingAlarm.id -> alarmReceiver.cancelAlarm(this, AlarmReceiver.Type.REPEATING)
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        // siapkan date formatternya
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        binding.tvOnceDate.text = dateFormat.format(calendar.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        // siapkan time formatter
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when (tag) {
            TIME_PICKER_ONCE_TAG -> binding.tvOnceTime.text = timeFormat.format(calendar.time)
            TIME_PICKER_REPEAT_TAG -> binding.tvRepeatingTime.text = timeFormat.format(calendar.time)
            else -> {}
        }
    }
}