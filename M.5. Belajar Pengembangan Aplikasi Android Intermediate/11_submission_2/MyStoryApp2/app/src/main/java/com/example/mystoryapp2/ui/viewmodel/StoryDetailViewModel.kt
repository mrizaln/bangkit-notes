package com.example.mystoryapp2.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mystoryapp2.model.repository.AppRepository

class StoryDetailViewModel(
    private val repository: AppRepository,
) : ViewModel() {
    fun getStory(id: String) = repository.getStoryDetail(id)
}