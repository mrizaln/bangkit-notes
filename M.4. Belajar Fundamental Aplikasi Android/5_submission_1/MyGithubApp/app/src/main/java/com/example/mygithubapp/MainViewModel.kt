package com.example.mygithubapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _queryTerm = MutableLiveData("arif")
    val queryTerm: LiveData<String> = _queryTerm

    private val _listUser = _queryTerm.switchMap {
        getUsers(it)
    }
    val listUser: LiveData<List<UserItemResponse?>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccessLoadUsers = MutableLiveData<Boolean>()
    val isSuccessLoadUsers: LiveData<Boolean> = _isSuccessLoadUsers

    init {
        _queryTerm.value = "arif"
    }

    fun search(name: String) {
        _queryTerm.value = name
    }

    private fun getUsers(name: String): MutableLiveData<List<UserItemResponse?>> {
        _isLoading.value = true
        val listUser = MutableLiveData<List<UserItemResponse?>>()

        val client = ApiConfig.getApiService().getUsers(name)
        client.enqueue(object : Callback<UserSearchResponse> {
            override fun onResponse(
                call: Call<UserSearchResponse>,
                response: Response<UserSearchResponse>,
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

                if (response.body()?.items == null) {
                    _isSuccessLoadUsers.value = false
                    return
                }

                listUser.value = response.body()?.items ?: listOf()
                _isSuccessLoadUsers.value = true
            }

            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                _isLoading.value = false
                _isSuccessLoadUsers.value = false
                Log.e(TAG, "onResponse: ${t.message.toString()}")
            }
        })

        return listUser
    }
}