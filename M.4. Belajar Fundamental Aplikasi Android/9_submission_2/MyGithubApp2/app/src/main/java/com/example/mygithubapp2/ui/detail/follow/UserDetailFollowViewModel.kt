package com.example.mygithubapp2.ui.detail.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mygithubapp2.data.UserRepository
import com.example.mygithubapp2.data.local.entity.UserEntity
import com.example.mygithubapp2.data.remote.RequestResult

class UserDetailFollowViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    fun getFollowers(username: String): LiveData<RequestResult<List<UserEntity>>> = userRepository.getFollowers(username)

    fun getFollowing(username: String): LiveData<RequestResult<List<UserEntity>>> = userRepository.getFollowing(username)
}