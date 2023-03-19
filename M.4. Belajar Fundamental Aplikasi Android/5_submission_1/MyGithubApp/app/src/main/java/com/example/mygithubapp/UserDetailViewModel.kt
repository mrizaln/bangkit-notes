package com.example.mygithubapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel : ViewModel() {
    companion object {
        private const val TAG = "UserDetailViewModel"
    }

    private var _username: String = ""

    private val _userData = MutableLiveData<UserDetailResponse>()
    val userData: LiveData<UserDetailResponse> = _userData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccessLoadUser = MutableLiveData<Boolean>()
    val isSuccessLoadUser: LiveData<Boolean> = _isSuccessLoadUser

    fun getUserDetail(username: String) {
        if (username == _username)
            return

        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>,
            ) {
                _isLoading.value = false
                if (!response.isSuccessful) {
                    _isSuccessLoadUser.value = false
                    Log.e(TAG, "onResponse: ${response.message()}")
                    return
                }

                if (response.body() == null) {
                    _isSuccessLoadUser.value = false
                    return
                }

                _userData.value = response.body()
                _isSuccessLoadUser.value = true

                _username = username
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _isSuccessLoadUser.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}