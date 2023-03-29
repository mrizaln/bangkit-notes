package com.example.mynotesapp2.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynotesapp2.databinding.ActivityMainBinding
import com.example.mynotesapp2.helper.ViewModelFactory
import com.example.mynotesapp2.ui.insert.NoteAddUpdateActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private val viewModel: MainViewModel by viewModels { ViewModelFactory.getInstance(application) }
//    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Notes"

        viewModel.getAllNotes().observe(this) { noteList ->
            if (noteList == null)
                return@observe
            noteAdapter.setListNotes(noteList)
        }

        noteAdapter = NoteAdapter()

        binding.rvNotes.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = noteAdapter
        }

        binding.fabAdd.setOnClickListener { view ->
            if (view != binding.fabAdd)
                return@setOnClickListener
            val intent = Intent(this@MainActivity, NoteAddUpdateActivity::class.java)
            startActivity(intent)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }
}