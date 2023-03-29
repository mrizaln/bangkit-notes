package com.example.barvolume

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.barvolume.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
//    private lateinit var edtWidth: EditText
//    private lateinit var edtHeight: EditText
//    private lateinit var edtLength: EditText
//    private lateinit var btnCalculate: Button
//    private lateinit var tvResult: TextView

    companion object {
        private const val STATE_RESULT = "state_result"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        edtWidth = findViewById(R.id.edt_width)
//        edtHeight = findViewById(R.id.edt_height)
//        edtLength = findViewById(R.id.edt_length)
//        btnCalculate = findViewById(R.id.btn_calculate)
//        tvResult = findViewById(R.id.tv_result)

//        btnCalculate.setOnClickListener(this)
        binding.btnCalculate.setOnClickListener(this)

        if (savedInstanceState != null) {
            val result = savedInstanceState.getString(STATE_RESULT)
//            tvResult.text = result
            binding.tvResult.text = result
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_calculate) {
            val (inputLength, inputWidth, inputHeight) = listOf(
                binding.edtLength, binding.edtWidth, binding.edtHeight
            ).map {
                it.text.toString().trim()
            }

            var isEmptyFields = false
            listOf(
                Pair(inputLength, binding.edtLength),
                Pair(inputWidth,  binding.edtWidth),
                Pair(inputHeight, binding.edtHeight),
            ).forEach{
                val (value, edt) = it
                if (value.isEmpty()) {
                    isEmptyFields = true
                    edt.error = "Field ini tidak boleh kosong"
                }
            }

            if (!isEmptyFields) {
                val volume = inputLength.toDouble() * inputWidth.toDouble() * inputHeight.toDouble()
                binding.tvResult.text = volume.toString()
            } else {
                binding.tvResult.text = "Field tidak boleh kosong"
            }
        }

        println("hi")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_RESULT, binding.tvResult.text.toString())
    }
}