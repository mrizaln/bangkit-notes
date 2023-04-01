package com.example.mygithubapp2.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygithubapp2.data.UserRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    fun getThemeSetting(): LiveData<Boolean> = userRepository.getThemeSetting()

    fun saveThemeSetting(isDarkMode: Boolean) = viewModelScope.launch {
        userRepository.saveThemeSetting(isDarkMode)
    }
}