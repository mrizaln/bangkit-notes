package com.dicoding.newsapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.newsapp.data.NewsRepository
import com.dicoding.newsapp.di.Injection

class ViewModelFactory private constructor(
    private  val newsRepository: NewsRepository
) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                INSTANCE!!
            }
        }
    }
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(NewsViewModel::class.java) -> NewsViewModel(newsRepository)
            else -> throw java.lang.IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        } as T
    }
}