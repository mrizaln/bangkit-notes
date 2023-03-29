package com.example.mysharedpreferences

import android.content.Context

internal class UserPreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val AGE = "age"
        private const val PHONE_NUMBER = "phone"
        private const val LOVE_MU = "islove"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: UserModel) {
        preferences.edit()
            .putString(NAME, value.name)
            .putString(EMAIL, value.email)
            .putInt(AGE, value.age)
            .putString(PHONE_NUMBER, value.phoneNumber)
            .putBoolean(LOVE_MU, value.isLove)
            .apply()
    }

    fun getUser(): UserModel {
        val p = preferences
        return UserModel(
            p.getString(NAME, ""),
            p.getString(EMAIL, ""),
            p.getInt(AGE, 0),
            p.getString(PHONE_NUMBER, ""),
            p.getBoolean(LOVE_MU, false)
        )
    }
}