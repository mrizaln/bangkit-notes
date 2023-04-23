package com.example.mystoryapp.data.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.data.repository.AppRepository
import com.example.mystoryapp.ui.activity.login.LoginViewModel
import com.example.mystoryapp.ui.activity.main.MainViewModel
import com.example.mystoryapp.ui.activity.register.RegisterViewModel
import com.example.mystoryapp.ui.activity.storyadd.StoryAddViewModel
import com.example.mystoryapp.ui.activity.storydetail.StoryDetailViewModel

class ViewModelFactory private constructor(
    private val repository: AppRepository,
) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            INSTANCE ?: synchronized(this) {
                ViewModelFactory(Injection.provideRepository(context)).also { INSTANCE = it }
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java)        -> {
                MainViewModel(repository)
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java)       -> {
                LoginViewModel(repository)
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java)    -> {
                RegisterViewModel(repository)
            }
            modelClass.isAssignableFrom(StoryAddViewModel::class.java)    -> {
                StoryAddViewModel(repository)
            }
            modelClass.isAssignableFrom(StoryDetailViewModel::class.java) -> {
                StoryDetailViewModel(repository)
            }
            else                                                          -> throw java.lang.IllegalArgumentException(
                "Unknown ViewModel class: " + modelClass.name
            )
        } as T
    }
}