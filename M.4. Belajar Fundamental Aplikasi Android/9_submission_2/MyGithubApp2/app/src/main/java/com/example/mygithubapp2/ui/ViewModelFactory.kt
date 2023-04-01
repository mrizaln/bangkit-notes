package com.example.mygithubapp2.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mygithubapp2.data.UserRepository
import com.example.mygithubapp2.di.Injection
import com.example.mygithubapp2.ui.detail.UserDetailViewModel
import com.example.mygithubapp2.ui.detail.follow.UserDetailFollowViewModel
import com.example.mygithubapp2.ui.favorite.FavoriteViewModel
import com.example.mygithubapp2.ui.main.MainViewModel
import com.example.mygithubapp2.ui.settings.SettingsViewModel

class ViewModelFactory private constructor(
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {
    companion object {
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
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository)
            }
            modelClass.isAssignableFrom(UserDetailViewModel::class.java) -> {
                UserDetailViewModel(userRepository)
            }
            modelClass.isAssignableFrom(UserDetailFollowViewModel::class.java) -> {
                UserDetailFollowViewModel(userRepository)
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(userRepository)
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(userRepository)
            }
            else -> throw java.lang.IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        } as T
    }
}