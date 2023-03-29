package com.example.mysharedpreferences

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.example.mysharedpreferences.databinding.ActivityFormUserPreferenceBinding

class FormUserPreferenceActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val EXTRA_TYPE_FORM = "extra_type_form"
        const val EXTRA_RESULT = "extra_result"
        const val RESULT_CODE = 101

        const val TYPE_ADD = 1
        const val TYPE_EDIT = 3

        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
        private const val FIELD_DIGIT_ONLY = "Hanya boleh terisi numerik"
        private const val EMAIL_IS_NOT_VALID = "Email tidak valid"
    }

    private lateinit var userModel: UserModel
    private lateinit var binding: ActivityFormUserPreferenceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFormUserPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener(this)

        userModel = intent.getParcelableExtra<UserModel>("USER") as UserModel
        val formType = intent.getIntExtra(EXTRA_TYPE_FORM, 0)

        var actionBarTitle = ""
        var btnTitle = ""

        when (formType) {
            TYPE_ADD -> {
                actionBarTitle = "Tambah Baru"
                btnTitle = "Simpan"
            }
            TYPE_EDIT -> {
                actionBarTitle = "Ubah"
                btnTitle = "Update"
                showPreferenceInForm()
            }
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSave.text = btnTitle
    }

    override fun onClick(p0: View?) {
        if (p0?.id == R.id.btn_save) {
            val name = binding.edtName.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val age = binding.edtAge.text.toString().trim()
            val phoneNo = binding.edtPhone.text.toString().trim()
            val isLoveMU = binding.rgLoveMu.checkedRadioButtonId == R.id.rb_yes

            if (name.isEmpty()) {
                binding.edtName.error = FIELD_REQUIRED
                return
            }
            if (email.isEmpty()) {
                binding.edtEmail.error = FIELD_REQUIRED
                return
            }
            if (!isValidEmail(email)) {
                binding.edtEmail.error = EMAIL_IS_NOT_VALID
                return
            }
            if (age.isEmpty()) {
                binding.edtAge.error = FIELD_REQUIRED
                return
            }
            if (phoneNo.isEmpty()) {
                binding.edtPhone.error = FIELD_REQUIRED
                return
            }
            if (!phoneNo.isDigitsOnly()) {
                binding.edtPhone.error = FIELD_DIGIT_ONLY
                return
            }

            saveUser(name, email, age, phoneNo, isLoveMU)
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_RESULT, userModel)
            setResult(RESULT_CODE, resultIntent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    private fun saveUser(name: String, email: String, age: String, phoneNumber: String, isLoveMU: Boolean) {
        val userPreference = UserPreference(this)

        userModel.apply {
            this.name = name
            this.email = email
            this.age = Integer.parseInt(age)
            this.phoneNumber = phoneNumber
            this.isLove = isLoveMU
        }

        userPreference.setUser(userModel)
        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show()
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showPreferenceInForm() {
        binding.apply {
            edtName.setText(userModel.name)
            edtEmail.setText(userModel.email)
            edtAge.setText(userModel.age.toString())
            edtPhone.setText(userModel.phoneNumber)
        }

        if (userModel.isLove)
            binding.rbYes.isChecked = true
        else
            binding.rbNo.isChecked = true
    }
}