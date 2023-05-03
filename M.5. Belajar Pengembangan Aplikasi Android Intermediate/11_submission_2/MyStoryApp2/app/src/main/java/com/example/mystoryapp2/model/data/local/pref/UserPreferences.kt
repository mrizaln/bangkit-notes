package com.example.mystoryapp2.model.data.local.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(
    private val dataStore: DataStore<Preferences>,
) {
    fun getUser(): Flow<User?> = dataStore.data.map { pref ->
        try {
            val user = User(
                pref[NAME_KEY]!!,
                pref[EMAIL_KEY]!!,
                pref[USER_ID_KEY]!!,
                pref[TOKEN_KEY]!!,
            )
            user
        } catch (e: NullPointerException) {
            null
        }
    }

    suspend fun saveUser(user: User) = dataStore.edit { pref ->
        pref[NAME_KEY] = user.name
        pref[EMAIL_KEY] = user.email
        pref[USER_ID_KEY] = user.userId
        pref[TOKEN_KEY] = user.token
    }

    suspend fun clearUser() = dataStore.edit { it.clear() }

    suspend fun updateUser(user: User) {
        clearUser()
        saveUser(user)
    }

    companion object {
        const val DATASTORE_NAME = "settings"

        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val USER_ID_KEY = stringPreferencesKey("userId")
        private val TOKEN_KEY = stringPreferencesKey("token")

        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences =
            INSTANCE ?: synchronized(this) {
                UserPreferences(dataStore).also { INSTANCE = it }
            }
    }
}