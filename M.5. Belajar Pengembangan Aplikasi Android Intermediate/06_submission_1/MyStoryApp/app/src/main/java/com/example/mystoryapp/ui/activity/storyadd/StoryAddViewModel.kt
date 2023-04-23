package com.example.mystoryapp.ui.activity.storyadd

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.repository.AppRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class StoryAddViewModel(
    private val repository: AppRepository,
) : ViewModel() {
    private val uploadInput = MutableLiveData<Pair<String, File>>()

    val uploadStatus = uploadInput.switchMap { (desc, file) ->
        repository.addNewStory(desc, file)
    }

    fun uploadImage(description: String, file: File): Job = viewModelScope.launch {
        uploadInput.value = Pair(description, file)
    }
}