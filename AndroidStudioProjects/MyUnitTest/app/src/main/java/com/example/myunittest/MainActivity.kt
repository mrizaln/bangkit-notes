package com.example.myunittest

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myunittest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        mainViewModel = MainViewModel(CuboidModel())

        activityMainBinding.apply {
            btnSave.setOnClickListener(this@MainActivity)
            btnCalculateCircumference.setOnClickListener(this@MainActivity)
            btnCalculateSurfaceArea.setOnClickListener(this@MainActivity)
            btnCalculateVolume.setOnClickListener(this@MainActivity)
        }
    }

    override fun onClick(p0: View?) {
        var error = false
        val (length, width, height) = listOf(
            activityMainBinding.edtLength,
            activityMainBinding.edtWidth,
            activityMainBinding.edtHeight,
        ).map { edt ->
            val text = edt.text.toString().trim()
            if (text.isEmpty()) {
                edt.error = "Field ini tidak boleh kosong"
                error = true
                0.0
            } else {
                text.toDouble()
            }
        }

        if (error) {
            gone()
            return
        }

        when (p0?.id) {
            R.id.btn_save -> {
                mainViewModel.save(width, length, height)
                visible()
            }
            R.id.btn_calculate_circumference -> {
                activityMainBinding.tvResult.text = mainViewModel.getCircumference().toString()
                gone()
            }
            R.id.btn_calculate_surface_area -> {
                activityMainBinding.tvResult.text = mainViewModel.getSurfaceArea().toString()
                gone()
            }
            R.id.btn_calculate_volume -> {
                activityMainBinding.tvResult.text = mainViewModel.getVolume().toString()
                gone()
            }
        }
    }

    private fun visible(gone: Boolean = false) {
        val visibility = { g: Boolean -> if (g) View.GONE else View.VISIBLE }
        activityMainBinding.apply {
            btnCalculateVolume.visibility = visibility(gone)
            btnCalculateCircumference.visibility = visibility(gone)
            btnCalculateSurfaceArea.visibility = visibility(gone)
            btnSave.visibility = visibility(!gone)
        }
    }

    private fun gone() {
        visible(gone = true)
    }
}