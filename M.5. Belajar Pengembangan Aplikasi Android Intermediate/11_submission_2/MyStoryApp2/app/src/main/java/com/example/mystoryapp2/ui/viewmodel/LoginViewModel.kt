package com.example.mystoryapp2.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp2.databinding.ActivityLoginBinding
import com.example.mystoryapp2.model.data.local.pref.User
import com.example.mystoryapp2.model.repository.AppRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AppRepository,
) : ViewModel() {
    private val loginInput = MutableLiveData<Pair<String, String>>()

    val loginStatus = loginInput.switchMap {
        repository.login(it.first, it.second)
//        Thread.sleep(2000)
//        MutableLiveData(RequestResult.Success(UserModel("success", "token")) as RequestResult<UserModel>)
    }

    fun login(email: String, password: String) {
        loginInput.value = Pair(email, password)
    }

    fun inputIsValid(binding: ActivityLoginBinding): Boolean {
        val noError = (binding.edLoginEmail.error == null) && (binding.edLoginPassword.error == null)
        val nonZeroLength = (binding.edLoginEmail.text.toString()
            .isNotEmpty())
                && (binding.edLoginPassword.text.toString()
            .isNotEmpty())
        return noError && nonZeroLength
    }

    fun addUser(user: User) = viewModelScope.launch { repository.addUserData(user) }

    fun holdToken(token: String) = repository.holdToken(token)
}