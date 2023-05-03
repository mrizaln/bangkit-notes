package com.example.mystoryapp2.model.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.mystoryapp2.model.data.local.database.StoryDatabase
import com.example.mystoryapp2.model.data.local.pref.UserPreferences
import com.example.mystoryapp2.model.data.remote.retrofit.ApiConfig
import com.example.mystoryapp2.model.repository.AppRepository


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = UserPreferences.DATASTORE_NAME
)

object Injection {
    fun provideRepository(context: Context): AppRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val tokenHolder = TokenHolder.getInstance(pref)
        val apiService = ApiConfig.getApiService(tokenHolder)
        val database = StoryDatabase.getDatabase(context)
        return AppRepository.getInstance(apiService, pref, tokenHolder, database)
    }
}