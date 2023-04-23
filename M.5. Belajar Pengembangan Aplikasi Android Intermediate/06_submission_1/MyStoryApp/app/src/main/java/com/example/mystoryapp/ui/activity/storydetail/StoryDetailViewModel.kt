package com.example.mystoryapp.ui.activity.storydetail

import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.repository.AppRepository

class StoryDetailViewModel(
    private val repository: AppRepository,
) : ViewModel() {
    fun getStory(id: String) = repository.getStoryDetail(id)
}