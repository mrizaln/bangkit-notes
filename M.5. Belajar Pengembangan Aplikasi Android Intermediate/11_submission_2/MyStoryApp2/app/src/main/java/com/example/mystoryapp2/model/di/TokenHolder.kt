package com.example.mystoryapp2.model.di

import android.util.Log
import com.example.mystoryapp2.model.data.local.pref.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class NullTokenException(msg: String) : Exception(msg)

class TokenHolder private constructor(
    private val preferences: UserPreferences,
) {
    private var _token: String? = null
    var token: String
        get() {
            return _token?.also {
                Log.d(javaClass.simpleName, "retrieving token normally")
            } ?: run {
                val newToken: String
                runBlocking(Dispatchers.IO) {
                    newToken = preferences.getUser().first()?.token
                        ?: throw NullTokenException("retrieved token is null")
                }
                Log.d(javaClass.simpleName, "retrieving token from preferences")
                newToken.also { _token = newToken }
            }
        }
        set(newToken) {
            _token = newToken
            Log.d(javaClass.simpleName, "token set")
        }

    companion object {
        @Volatile
        private var INSTANCE: TokenHolder? = null

        fun getInstance(preferences: UserPreferences) = INSTANCE ?: synchronized(this) {
            TokenHolder(preferences).also { INSTANCE = it }
        }
    }
}