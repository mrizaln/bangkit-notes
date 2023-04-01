package com.example.mygithubapp2.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.mygithubapp2.data.UserRepository

class MainViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _queryTerm = MutableLiveData("arif")
    val queryTerm: LiveData<String> = _queryTerm

    val listUser = _queryTerm.switchMap {
        userRepository.getUsers(it)
    }

    fun search(name: String) {
        _queryTerm.value = name
    }

    fun getThemeSetting(): LiveData<Boolean> = userRepository.getThemeSetting()
}