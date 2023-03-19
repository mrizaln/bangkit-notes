package com.example.mygithubapp

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailFollowViewModel : ViewModel() {
    companion object {
        private const val TAG = "UserDetailFollowViewModel"
    }

    private val _username: String = ""

    private val _listUser = MutableLiveData<List<UserItemResponse?>>()
    val listUser: LiveData<List<UserItemResponse?>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccessLoadUsers = MutableLiveData<Boolean>()
    val isSuccessLoadUsers: LiveData<Boolean> = _isSuccessLoadUsers

    fun getUsers(username: String, follow: UserDetailActivity.Companion.Follow) {
        if (_username == username) return

        _isLoading.value = true
        val client = when (follow) {
            UserDetailActivity.Companion.Follow.FOLLOWERS -> ApiConfig.getApiService().getFollowers(username)
            UserDetailActivity.Companion.Follow.FOLLOWING -> ApiConfig.getApiService().getFollowing(username)
        }

        client.enqueue(object : Callback<List<UserItemResponse>> {
            @SuppressLint("LongLogTag")
            override fun onResponse(
                call: Call<List<UserItemResponse>>,
                response: Response<List<UserItemResponse>>,
            ) {
                _isLoading.value = false
                if (!response.isSuccessful) {
                    _isSuccessLoadUsers.value = false
                    Log.e(TAG, "onResponse: ${response.message()}")
                    return
                }

                if (response.body() == null) {
                    _isSuccessLoadUsers.value = false
                    return
                }

                if (response.body() == null) {
                    _isSuccessLoadUsers.value = false
                    return
                }

                _listUser.value = response.body()
                _isSuccessLoadUsers.value = true
            }

            @SuppressLint("LongLogTag")
            override fun onFailure(call: Call<List<UserItemResponse>>, t: Throwable) {
                _isLoading.value = false
                _isSuccessLoadUsers.value = false
                Log.e(TAG, "onResponse: ${t.message.toString()}")
            }
        })
    }

}