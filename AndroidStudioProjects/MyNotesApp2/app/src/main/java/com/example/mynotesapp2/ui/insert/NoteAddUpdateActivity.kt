package com.example.mynotesapp2.ui.insert

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mynotesapp2.R
import com.example.mynotesapp2.database.Note
import com.example.mynotesapp2.databinding.ActivityNoteAddUpdateBinding
import com.example.mynotesapp2.helper.DateHelper
import com.example.mynotesapp2.helper.ViewModelFactory

class NoteAddUpdateActivity : AppCompatActivity() {
    enum class Extra(val string: String) {
        NOTE("extra_note"),
    }

    private enum class AlertType(val code: Int) {
        DIALOG_CLOSE(10),
        DIALOG_DELETE(20),
    }

    private enum class Mode {
        ADD,
        EDIT,
    }

    private lateinit var mode: Mode
    private lateinit var note: Note

    private lateinit var binding: ActivityNoteAddUpdateBinding
    private val viewModel: NoteAddUpdateViewModel by viewModels { ViewModelFactory.getInstance(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentNote: Note? = intent.getParcelableExtra(Extra.NOTE.string)
        if (intentNote == null) {
            note = Note()
            mode = Mode.ADD
        } else {
            note = intentNote
            mode = Mode.EDIT
        }

        val actionbarTitle: String
        val btnTitle: String
        when (mode) {
            Mode.ADD -> {
                actionbarTitle = getString(R.string.add)
                btnTitle = getString(R.string.save)
            }
            Mode.EDIT -> {
                actionbarTitle = getString(R.string.change)
                btnTitle = getString(R.string.update)

                binding.edtTitle.setText(note.title)
                binding.edtDescription.setText(note.description)
            }
        }

        supportActionBar?.title = actionbarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSubmit.text = btnTitle

        binding.btnSubmit.setOnClickListener { view -> submit(view) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (mode == Mode.EDIT)
            menuInflater.inflate(R.menu.menu_form, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(AlertType.DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(AlertType.DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        showAlertDialog(AlertType.DIALOG_CLOSE)
    }

    private fun submit(view: View) {
        val noteTitle = binding.edtTitle.text.toString().trim()
        val noteDescription = binding.edtDescription.text.toString().trim()

        val isError = when {
            noteTitle.isEmpty() -> {
                binding.edtTitle.error = getString(R.string.empty)
                true
            }
            noteDescription.isEmpty() -> {
                binding.edtDescription.error = getString(R.string.empty)
                true
            }
            else -> false
        }
        if (isError)
            return

        note.apply {
            title = noteTitle
            description = noteDescription
        }

        when (mode) {
            Mode.ADD -> {
                note.date = DateHelper.getCurrentDate()
                viewModel.insert(note)
                showToast(getString(R.string.added))
            }
            Mode.EDIT -> {
                viewModel.update(note)
                showToast(getString(R.string.changed))
            }
        }
        finish()
    }

    private fun deleteNote() {
        viewModel.delete(note)
        showToast(getString(R.string.deleted))
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showAlertDialog(type: AlertType) {
        val dialogTitle: String
        val dialogMessage: String
        when (type) {
            AlertType.DIALOG_CLOSE -> {
                dialogTitle = getString(R.string.cancel)
                dialogMessage = getString(R.string.message_cancel)
            }
            AlertType.DIALOG_DELETE -> {
                dialogTitle = getString(R.string.delete)
                dialogMessage = getString(R.string.message_delete)
            }
        }
        val dialog = AlertDialog.Builder(this)
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                when (type) {
                    AlertType.DIALOG_CLOSE -> {}
                    AlertType.DIALOG_DELETE -> deleteNote()
                }
                finish()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .create()
        dialog.show()
    }

}