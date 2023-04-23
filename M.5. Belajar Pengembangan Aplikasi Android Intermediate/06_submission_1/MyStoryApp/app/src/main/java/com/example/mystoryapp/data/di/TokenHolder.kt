package com.example.mystoryapp.data.di

class NullTokenException(msg: String) : Exception(msg)

class TokenHolder private constructor() {
    private var _token: String? = null
    var token: String
        get() = _token ?: throw NullTokenException("Token is null")
        set(value) {
            _token = value
        }

    companion object {
        @Volatile
        private var INSTANCE: TokenHolder? = null

        fun getInstance() = INSTANCE ?: synchronized(this) {
            TokenHolder().also { INSTANCE = it }
        }
    }
}