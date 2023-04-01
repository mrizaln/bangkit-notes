package com.example.mygithubapp2.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.mygithubapp2.data.UserRepository
import com.example.mygithubapp2.data.datastore.SettingPreferences
import com.example.mygithubapp2.data.local.UserDatabase
import com.example.mygithubapp2.data.remote.retrofit.ApiConfig

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SettingPreferences.DATASTORE_NAME)

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val dao = UserDatabase.getInstance(context).userDao()
        val pref = SettingPreferences.getInstance(context.dataStore)
        return UserRepository.getInstance(apiService, dao, pref)
    }
}