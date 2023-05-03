package com.example.mystoryapp2.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp2.model.data.local.database.Story
import com.example.mystoryapp2.model.repository.AppRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: AppRepository,
) : ViewModel() {
    val stories: LiveData<PagingData<Story>> =
        repository.getPagedStories().cachedIn(viewModelScope)

    fun getUserData() = repository.getUserData()

    fun holdToken(token: String) = repository.holdToken(token)

    fun logout() = viewModelScope.launch { repository.clearUserData() }
}