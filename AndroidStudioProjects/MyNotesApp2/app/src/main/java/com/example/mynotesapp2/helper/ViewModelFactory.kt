package com.example.mynotesapp2.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mynotesapp2.ui.insert.NoteAddUpdateActivity
import com.example.mynotesapp2.ui.insert.NoteAddUpdateViewModel
import com.example.mynotesapp2.ui.main.MainViewModel

class ViewModelFactory
private constructor(
    private val mApplication: Application,
) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            return INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE = ViewModelFactory(application)
                INSTANCE!!
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(mApplication)
            }
            modelClass.isAssignableFrom(NoteAddUpdateViewModel::class.java) -> {
                NoteAddUpdateViewModel(mApplication)
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T
    }
}