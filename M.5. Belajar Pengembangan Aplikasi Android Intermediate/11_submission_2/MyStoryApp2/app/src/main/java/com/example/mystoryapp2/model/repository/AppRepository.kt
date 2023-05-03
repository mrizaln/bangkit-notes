package com.example.mystoryapp2.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.mystoryapp2.model.data.local.database.Story
import com.example.mystoryapp2.model.data.local.database.StoryDatabase
import com.example.mystoryapp2.model.data.local.pref.User
import com.example.mystoryapp2.model.data.local.pref.UserPreferences
import com.example.mystoryapp2.model.data.remote.response.*
import com.example.mystoryapp2.model.data.remote.retrofit.ApiConfig
import com.example.mystoryapp2.model.data.remote.retrofit.ApiService
import com.example.mystoryapp2.model.di.TokenHolder
import com.example.mystoryapp2.util.wrapEspressoIdlingResource
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class AppRepository private constructor(
    private val apiService: ApiService,
    private val preferences: UserPreferences,
    private val tokenHolder: TokenHolder,
    private val database: StoryDatabase,
) {
    fun holdToken(token: String) {
        tokenHolder.token = token
    }

    fun login(
        email: String,
        password: String,
    ): LiveData<RequestResult<User>> = liveData {
        emit(RequestResult.Loading)

        wrapEspressoIdlingResource {
            try {
                val response = apiService.login(email, password)
                if (response.error || response.loginResult == null) {
                    throw Exception(response.message)
                }

                val data = response.loginResult
                val user = User(data.name, email, data.userId, data.token)

                emitSource(MutableLiveData(RequestResult.Success(user)))
            } catch (e: HttpException) {
                val message = getResponse(e, LoginResponse::class.java).message
                emit(RequestResult.Error(message))
            } catch (e: Exception) {
                emit(RequestResult.Error(e.message.toString()))
            }
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
    ): LiveData<RequestResult<String>> = liveData {
        emit(RequestResult.Loading)

        wrapEspressoIdlingResource {
            try {
                val response = apiService.register(name, email, password)
                if (response.error) throw Exception(response.message)

                val message = response.message
                emitSource(MutableLiveData(RequestResult.Success(message)))
            } catch (e: HttpException) {
                val message = getResponse(e, RegisterResponse::class.java).message
                emit(RequestResult.Error(message))
            } catch (e: Exception) {
                emit(RequestResult.Error(e.message.toString()))
            }
        }
    }

    fun getStories(
        page: Int? = null,
        size: Int? = null,
        withLocationOnly: Boolean? = null,
    ): LiveData<RequestResult<List<Story>>> = liveData {
        emit(RequestResult.Loading)

        wrapEspressoIdlingResource {
            try {
                val location = withLocationOnly?.let { if (it) 1 else 0 }
                val response = apiService.getStories(page, size, location)
                if (response.error) throw Exception(response.message)

                val storyList = response.listStory.map {
                    Story(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        photoUrl = it.photoUrl,
                        createdAt = it.createdAt,
                        lon = it.lon,
                        lat = it.lat
                    )
                }

                emitSource(MutableLiveData(RequestResult.Success(storyList)))
            } catch (e: HttpException) {
                val message = getResponse(e, StoryListResponse::class.java).message
                emit(RequestResult.Error(message))
            } catch (e: Exception) {
                emit(RequestResult.Error(e.message.toString()))
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getPagedStories(): LiveData<PagingData<Story>> {
        Log.d(javaClass.simpleName, "try to load data")

        return Pager(
            config = PagingConfig(
                pageSize = StoryRemoteMediator.PAGE_SIZE,
                prefetchDistance = StoryRemoteMediator.PREFETCH_SIZE,
                enablePlaceholders = true,
                maxSize = StoryRemoteMediator.MAX_SIZE
            ),
            remoteMediator = StoryRemoteMediator(database, apiService),
            pagingSourceFactory = database.storyDao()::getStories
        ).liveData
    }

    fun getStoryDetail(
        id: String,
    ): LiveData<RequestResult<Story>> = liveData {
        emit(RequestResult.Loading)

        wrapEspressoIdlingResource {
            try {
                val response = apiService.getStoryDetail(id)
                if (response.error) throw Exception(response.message)

                val story = response.story?.let {
                    Story(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        photoUrl = it.photoUrl,
                        createdAt = it.createdAt,
                    )
                } ?: throw IllegalStateException(
                    "story should not be null (story will have null value only on error): ${response.message}"
                )

                emitSource(MutableLiveData(RequestResult.Success(story)))
            } catch (e: HttpException) {
                val message = getResponse(e, StoryDetailResponse::class.java).message
                emit(RequestResult.Error(message))
            } catch (e: Exception) {
                emit(RequestResult.Error(e.message.toString()))
            }
        }
    }

    fun addNewStory(
        description: String,
        file: File,
        lat: Float?,
        lon: Float?,
    ): LiveData<RequestResult<String>> = liveData {
        emit(RequestResult.Loading)

        wrapEspressoIdlingResource {
            try {
                val requestDescription = description.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val requestImageMultipart = MultipartBody.Part.createFormData(
                    ApiConfig.POST_STORY_IMAGE_NAME, file.name, requestImageFile
                )

                val response = apiService.postStory(
                    requestImageMultipart, requestDescription, lat, lon
                )
                if (response.error) throw Exception(response.message)

                emitSource(MutableLiveData(RequestResult.Success(response.message)))
            } catch (e: HttpException) {
                val message = getResponse(e, NewStoryResponse::class.java).message
                emit(RequestResult.Error(message))
            } catch (e: Exception) {
                emit(RequestResult.Error(e.message.toString()))
                Log.d("addNewStory", e.message.toString())
            }
        }
    }

    fun getUserData() = preferences.getUser().asLiveData()
    suspend fun clearUserData() = preferences.clearUser()
    suspend fun addUserData(user: User) = preferences.saveUser(user)

    private fun <T> getResponse(e: HttpException, clazz: Class<T>): T {
        val errorBody = e.response()?.errorBody()?.string() ?: "{\"message\": \"unknown error\"}"
        return Gson().getAdapter(clazz).fromJson(errorBody)
    }

    companion object {
        @Volatile
        private var INSTANCE: AppRepository? = null

        fun getInstance(
            apiService: ApiService,
            preferences: UserPreferences,
            tokenHolder: TokenHolder,
            database: StoryDatabase,
        ): AppRepository = INSTANCE ?: synchronized(this) {
            AppRepository(apiService, preferences, tokenHolder, database).also { INSTANCE = it }
        }
    }
}