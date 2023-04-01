package com.example.mygithubapp2.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygithubapp2.data.UserRepository
import com.example.mygithubapp2.data.local.entity.UserEntity
import com.example.mygithubapp2.data.remote.RequestResult
import kotlinx.coroutines.launch

class UserDetailViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    fun getUserDetail(username: String): LiveData<RequestResult<UserDetail>> =
        userRepository.getUserDetail(username)

    fun getFavoriteUserByUsername(username: String): LiveData<UserEntity> =
        userRepository.getFavoriteUserByUsername(username)

    fun removeFavoriteUserByUsername(username: String) {
        viewModelScope.launch { userRepository.removeFavoriteUserByUsername(username) }
    }

    fun addUserToFavorite(user: UserEntity) {
        viewModelScope.launch { userRepository.addUserToFavorite(user) }
    }
}