package com.example.myworkmanager

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.example.myworkmanager.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workManager = WorkManager.getInstance(this)
        binding.apply {
            btnOneTimeTask.setOnClickListener(this@MainActivity)
            btnPeriodicTask.setOnClickListener(this@MainActivity)
            btnCancelTask.setOnClickListener(this@MainActivity)
        }
    }

    override fun onClick(view: View) {
        val b = binding
        when (view.id) {
            b.btnOneTimeTask.id -> startOneTimeTask()
            b.btnPeriodicTask.id -> startPeriodicTask()
            b.btnCancelTask.id -> cancelPeriodicTask()
        }
    }

    private fun startOneTimeTask() {
        binding.textStatus.text = getString(R.string.status)
        val data = Data.Builder()
            .putString(MyWorker.EXTRA_CITY, binding.editCity.text.toString())
            .build()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInputData(data)
            .setConstraints(constraints)
            .build()
        workManager.enqueue(oneTimeWorkRequest)
        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
            .observe(this@MainActivity) { workInfo ->
                val status = workInfo.state.name
                binding.textStatus.append("\n" + status)
            }
    }

    private fun startPeriodicTask() {
        binding.textStatus.text = getString(R.string.status)
        val data = Data.Builder()
            .putString(MyWorker.EXTRA_CITY, binding.editCity.text.toString())
            .build()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        periodicWorkRequest = PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
            .setInputData(data)
            .setConstraints(constraints)
            .build()
        workManager.enqueue(periodicWorkRequest)
        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(this@MainActivity) { workInfo ->
                val status = workInfo.state.name
                binding.textStatus.append("\n" + status)
                binding.btnCancelTask.isEnabled = false
                if (workInfo.state == WorkInfo.State.ENQUEUED)
                    binding.btnCancelTask.isEnabled = true
            }
    }

    private fun cancelPeriodicTask() {
        workManager.cancelWorkById(periodicWorkRequest.id)
    }
}