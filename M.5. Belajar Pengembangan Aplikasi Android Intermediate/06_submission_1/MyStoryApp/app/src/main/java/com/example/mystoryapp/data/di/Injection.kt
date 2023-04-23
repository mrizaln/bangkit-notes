package com.example.mystoryapp.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.mystoryapp.data.model.UserPreferences
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import com.example.mystoryapp.data.repository.AppRepository


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = UserPreferences.DATASTORE_NAME
)

object Injection {
    fun provideRepository(context: Context): AppRepository {
        val tokenHolder = TokenHolder.getInstance()
        val apiService = ApiConfig.getApiService(tokenHolder)
        val pref = UserPreferences.getInstance(context.dataStore)
        return AppRepository.getInstance(apiService, pref, tokenHolder)
    }
}