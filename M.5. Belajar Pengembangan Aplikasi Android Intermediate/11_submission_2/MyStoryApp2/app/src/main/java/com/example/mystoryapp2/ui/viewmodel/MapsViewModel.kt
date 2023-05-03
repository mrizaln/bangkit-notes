package com.example.mystoryapp2.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.mystoryapp2.model.repository.AppRepository

class MapsViewModel(
    private val repository: AppRepository,
) : ViewModel() {

    private val querySize = MutableLiveData(1)

    val stories = querySize.switchMap { size ->
        repository.getStories(
            size = size,
            withLocationOnly = true
        ).also {
            Log.d(javaClass.simpleName, "stories data GET")
        }
    }

    fun setQuerySize(size: Int) {
        querySize.value = size
    }

    fun reset(size: Int) {
        querySize.value = size
    }
}