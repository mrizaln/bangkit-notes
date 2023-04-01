package com.example.mygithubapp2.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.mygithubapp2.data.datastore.SettingPreferences
import com.example.mygithubapp2.data.local.UserDao
import com.example.mygithubapp2.data.local.entity.UserEntity
import com.example.mygithubapp2.data.remote.RequestResult
import com.example.mygithubapp2.data.remote.retrofit.ApiService
import com.example.mygithubapp2.ui.detail.UserDetail
import com.example.mygithubapp2.ui.detail.UserDetailActivity

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val preferences: SettingPreferences
) {
    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(apiService: ApiService, userDao: UserDao, preferences: SettingPreferences): UserRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = UserRepository(apiService, userDao, preferences)
                INSTANCE!!
            }
        }
    }

    fun getUsers(term: String): LiveData<RequestResult<List<UserEntity>>> = liveData {
        emit(RequestResult.Loading)

        val users = ArrayList<UserEntity>()
        try {
            val response = apiService.getUsers(term)
            val usersResponse = (response.items ?: listOf())
                .map { userResponse ->
                    val username = userResponse?.login ?: ""
                    val avatarUrl = userResponse?.avatarUrl ?: ""
                    UserEntity(username, avatarUrl)
                }
            users.addAll(usersResponse)
        } catch (e: Exception) {
            Log.d(
                this@UserRepository::class.java.simpleName,
                "${::getUsers.name} error: ${e.message.toString()}"
            )
            emit(RequestResult.Error(e.message.toString()))
        }

        val data = MutableLiveData<RequestResult<List<UserEntity>>>(RequestResult.Success(users))
        emitSource(data)
    }

    fun getUserDetail(username: String): LiveData<RequestResult<UserDetail>> = liveData {
        emit(RequestResult.Loading)

        val data = MutableLiveData<RequestResult<UserDetail>>()
        try {
            val response = apiService.getUser(username)
            val userDetail = UserDetail(
                response.login?: "",
                response.name ?: "",
                response.followers ?: 0,
                response.following ?: 0,
                response.avatarUrl ?: "",
            )
            data.value = RequestResult.Success(userDetail)
        } catch (e: Exception) {
            Log.d(
                this@UserRepository::class.java.simpleName,
                "${::getUserDetail.name} error: ${e.message.toString()}",
            )
            emit(RequestResult.Error(e.message.toString()))
        }

        emitSource(data)
    }

    fun getFollowers(username: String): LiveData<RequestResult<List<UserEntity>>> =
        getUsersFollow(username, UserDetailActivity.Follow.FOLLOWERS)

    fun getFollowing(username: String): LiveData<RequestResult<List<UserEntity>>> =
        getUsersFollow(username, UserDetailActivity.Follow.FOLLOWING)

    private fun getUsersFollow(
        username: String,
        follow: UserDetailActivity.Follow,
    ): LiveData<RequestResult<List<UserEntity>>> = liveData {
        emit(RequestResult.Loading)

        val data = MutableLiveData<RequestResult<List<UserEntity>>>()
        try {
            val response = when (follow) {
                UserDetailActivity.Follow.FOLLOWERS -> apiService.getFollowers(username)
                UserDetailActivity.Follow.FOLLOWING -> apiService.getFollowing(username)
            }
            val users = response.map { item ->
                UserEntity(item.login!!, item.avatarUrl ?: "")
            }
            data.value = RequestResult.Success(users)
        } catch (e: Exception) {
            emit(RequestResult.Error(e.message.toString()))
        }

        emitSource(data)
    }

    // favorites
    fun getFavoriteUsers(): LiveData<List<UserEntity>> = userDao.getUsers()
    fun getFavoriteUserByUsername(username: String): LiveData<UserEntity> = userDao.getUserByUsername(username)
    suspend fun removeFavoriteUsers() = userDao.deleteAll()
    suspend fun removeFavoriteUserByUsername(username: String) = userDao.deleteUserByUsername(username)
    suspend fun addUserToFavorite(user: UserEntity) = userDao.insertUser(user)

    // theme
    fun getThemeSetting(): LiveData<Boolean> = preferences.getThemeSetting().asLiveData()
    suspend fun saveThemeSetting(isDarkMode: Boolean) = preferences.saveThemeSetting(isDarkMode)
}