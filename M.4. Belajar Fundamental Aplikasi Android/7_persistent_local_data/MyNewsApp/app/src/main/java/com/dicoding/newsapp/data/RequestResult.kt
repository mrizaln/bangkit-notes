package com.dicoding.newsapp.data

sealed class RequestResult<out R> private constructor() {
    data class Success<out T>(val data: T): RequestResult<T>()
    data class Error(val error: String): RequestResult<Nothing>()
    object Loading : RequestResult<Nothing>()
}
