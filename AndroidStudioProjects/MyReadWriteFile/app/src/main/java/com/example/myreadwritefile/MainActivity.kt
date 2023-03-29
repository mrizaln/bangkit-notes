package com.example.myreadwritefile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myreadwritefile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            buttonNew.setOnClickListener(this@MainActivity)
            buttonOpen.setOnClickListener(this@MainActivity)
            buttonSave.setOnClickListener(this@MainActivity)
        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.button_new -> newFile()
            R.id.button_open -> showList()
            R.id.button_save -> saveFile()
        }
    }

    private fun newFile() {
        binding.editTitle.setText("")
        binding.editFile.setText("")
        Toast.makeText(this, "Clearing file", Toast.LENGTH_SHORT).show()
    }

    private fun showList() {
        val items = fileList()
        Log.d("ITEMS", items.size.toString())
        for (item in items)
            Toast.makeText(this@MainActivity, item.toString(), Toast.LENGTH_SHORT).show()
        val alert = AlertDialog.Builder(this)
            .setTitle("Pilih file yang diinginkan")
            .setItems(items) { _, item -> loadData(items[item].toString()) }
            .create()
        alert.show()
    }

    private fun loadData(title: String) {
        val fileModel = FileHelper.readFromFile(this, title)
        binding.editTitle.setText(fileModel.filename)
        binding.editFile.setText(fileModel.data)
        Toast.makeText(this, "Loading " + fileModel.filename + " data", Toast.LENGTH_SHORT).show()
    }

    private fun saveFile() {
        when {
            binding.editTitle.text.toString().isEmpty() -> Toast.makeText(
                this, "Title harus diisi terlebih dahulu", Toast.LENGTH_SHORT
            ).show()
            binding.editFile.text.toString().isEmpty() -> Toast.makeText(
                this, "Content harus diisi terlebih dahulu", Toast.LENGTH_SHORT
            ).show()
            else -> {
                val title = binding.editTitle.text.toString()
                val text = binding.editFile.text.toString()
                val fileModel = FileModel(title, text)
                FileHelper.writeToFile(fileModel, this)
                Toast.makeText(this, "Saving " + fileModel.filename + " file", Toast.LENGTH_SHORT).show()
            }
        }
    }
}