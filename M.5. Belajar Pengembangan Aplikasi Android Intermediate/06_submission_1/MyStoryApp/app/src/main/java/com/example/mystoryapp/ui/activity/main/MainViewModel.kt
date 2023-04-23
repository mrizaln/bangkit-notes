package com.example.mystoryapp.ui.activity.main

import androidx.lifecycle.*
import com.example.mystoryapp.data.model.StoryModel
import com.example.mystoryapp.data.repository.AppRepository
import com.example.mystoryapp.data.repository.RequestResult
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: AppRepository,
) : ViewModel() {
    private var pagination = 0
    private val storiesPagination = MutableLiveData<Int>()

    private val storyList = arrayListOf<StoryModel>()

    val stories = storiesPagination.switchMap { page ->
        repository.getStories(page).map { result ->
            return@map when (result) {
                is RequestResult.Loading -> RequestResult.Loading
                is RequestResult.Error   -> RequestResult.Error(result.error)
                is RequestResult.Success -> {
                    storyList.addAll(result.data)
                    RequestResult.Success(storyList)
                }
            }
        }
    }

    fun getUserData() = repository.getUserData()

    fun holdToken(token: String) = repository.holdToken(token)

    fun loadMoreStories() {
        storiesPagination.value = pagination++
    }

    fun resetPagination() {
        storyList.clear()
        pagination = 0
        storiesPagination.value = pagination++
    }

    fun logout() = viewModelScope.launch { repository.clearUserData() }
}