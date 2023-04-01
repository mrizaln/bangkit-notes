package com.example.mygithubapp2.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygithubapp2.data.UserRepository
import com.example.mygithubapp2.data.local.entity.UserEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    fun getUsers(): LiveData<List<UserEntity>> = userRepository.getFavoriteUsers()

    fun removeUsers() = viewModelScope.launch {
        userRepository.removeFavoriteUsers()
    }
}