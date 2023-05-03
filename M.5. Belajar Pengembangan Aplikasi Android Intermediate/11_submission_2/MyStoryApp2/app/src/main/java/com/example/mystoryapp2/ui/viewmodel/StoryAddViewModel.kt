package com.example.mystoryapp2.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp2.model.repository.AppRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class StoryAddViewModel(
    private val repository: AppRepository,
) : ViewModel() {
    private val uploadInput = MutableLiveData<UploadData>()

    val uploadStatus = uploadInput.switchMap { data ->
        repository.addNewStory(data.description, data.file, data.lat, data.lon)
    }

    fun uploadImage(description: String, file: File, lat: Float? = null, lon: Float? = null): Job =
        viewModelScope.launch {
            uploadInput.value = UploadData(description, file, lat, lon)
        }

    private data class UploadData(
        val description: String,
        val file: File,
        val lat: Float?,
        val lon: Float?,
    )
}