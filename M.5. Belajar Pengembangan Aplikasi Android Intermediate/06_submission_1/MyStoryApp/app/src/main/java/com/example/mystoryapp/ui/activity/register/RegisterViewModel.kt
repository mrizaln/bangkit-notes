package com.example.mystoryapp.ui.activity.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.mystoryapp.data.repository.AppRepository
import com.example.mystoryapp.databinding.ActivityRegisterBinding

class RegisterViewModel(
    private val repository: AppRepository,
) : ViewModel() {
    private val registerInput = MutableLiveData<Triple<String, String, String>>()

    val registerStatus = registerInput.switchMap {
        repository.register(it.first, it.second, it.third)
    }

    fun register(name: String, email: String, password: String) {
        registerInput.value = Triple(name, email, password)
    }

    fun inputIsValid(binding: ActivityRegisterBinding): Boolean {
        val noError = (binding.edRegisterName.error == null) && (binding.edRegisterEmail.error == null) && (binding.edRegisterPassword.error == null)
        val nonZeroLength = (binding.edRegisterName.text.toString()
            .isNotEmpty()) && (binding.edRegisterEmail.text.toString()
            .isNotEmpty()) && (binding.edRegisterPassword.text.toString()
            .isNotEmpty())
        return noError && nonZeroLength
    }
}