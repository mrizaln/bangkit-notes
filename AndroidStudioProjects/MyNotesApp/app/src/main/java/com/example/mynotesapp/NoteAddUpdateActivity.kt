package com.example.mynotesapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mynotesapp.databinding.ActivityNoteAddUpdateBinding
import com.example.mynotesapp.db.DatabaseContract.NoteColumns
import com.example.mynotesapp.db.NoteHelper
import com.example.mynotesapp.entity.Note
import java.text.SimpleDateFormat
import java.util.*

class NoteAddUpdateActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val RESULT_ADD = 101
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val RESULT_CANCEL = 100
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    private var isEdit = false
    private var note: Note? = null
    private var position = 0

    private lateinit var noteHelper: NoteHelper
    private lateinit var binding: ActivityNoteAddUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteHelper = NoteHelper.getInstance(applicationContext)
        noteHelper.open()

        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note == null) {
            note = Note()
        } else {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = "Ubah"
            btnTitle = "Update"

            note?.let {
                binding.edtTitle.setText(it.title)
                binding.edtDescription.setText(it.description)
            }
        } else {
            actionBarTitle = "Tambah"
            btnTitle = "Simpan"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSubmit.text = btnTitle
        binding.btnSubmit.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == binding.btnSubmit.id) {
            val title = binding.edtTitle.text.toString().trim()
            val description = binding.edtDescription.text.toString().trim()

            if (title.isEmpty()) {
                binding.edtTitle.error = "Field can not be blank"
                return
            }

            note?.title = title
            note?.description = description

            val intent = Intent()
            intent.apply {
                putExtra(EXTRA_NOTE, note)
                putExtra(EXTRA_POSITION, position)
            }

            val values = ContentValues()
            values.apply {
                put(NoteColumns.TITLE, title)
                put(NoteColumns.DESCRIPTION, description)
            }

            if (isEdit) {
                val result = noteHelper.update(note?.id.toString(), values).toLong()
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                } else {
                    Toast.makeText(this@NoteAddUpdateActivity, "Gagal mengupdate data", Toast.LENGTH_SHORT).show()
                }
            } else {
                note?.date = getCurrentDate()
                values.put(NoteColumns.DATE, getCurrentDate())
                val result = noteHelper.insert(values)

                if (result > 0) {
                    note?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    finish()
                } else {
                    Toast.makeText(this@NoteAddUpdateActivity, "Gagal menambah data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyy/MM/dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit)
            menuInflater.inflate(R.menu.menu_form, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun showAlertDialog(type: Int) {
        var dialogTitle: String = "Error"
        var dialogMessage: String = "Error"

        when (type) {
            ALERT_DIALOG_CLOSE -> {
                dialogTitle = "Batal"
                dialogMessage = "Apakah anda inigin membatalkan perubahan pada form?"
            }
            ALERT_DIALOG_DELETE -> {
                dialogTitle = "Hapus Note"
                dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
            }
        }

        val alertDialog = AlertDialog.Builder(this)
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("ya") { _, _ ->
                if (type == ALERT_DIALOG_CLOSE) {
                    setResult(RESULT_CANCEL, intent)
                    finish()
                } else {
                    val result = noteHelper.deleteById(note?.id.toString()).toLong()
                    if (result > 0) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    } else {
                        Toast.makeText(this@NoteAddUpdateActivity, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
            .create()
        alertDialog.show()
    }
}