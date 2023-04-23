package com.example.mystoryapp.data.remote.retrofit

import android.util.Log
import com.example.mystoryapp.BuildConfig
import com.example.mystoryapp.data.di.TokenHolder
import com.example.mystoryapp.data.remote.response.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class RequiresAuth

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): RegisterResponse

    @RequiresAuth
    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query(
            "location"
        ) location: Int? = 0,       // possible values: 0, 1 (Boolean represented as Int)
    ): StoryListResponse

    @RequiresAuth
    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Path("id") id: String,
    ): StoryDetailResponse

    @RequiresAuth
    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): NewStoryResponse
}

object ApiConfig {
    private const val ENDPOINT = "https://story-api.dicoding.dev/v1/"
    const val POST_STORY_IMAGE_NAME = "photo"

    fun getApiService(tokenHolder: TokenHolder): ApiService {
        val clientBuilder = OkHttpClient.Builder()

        val authInterceptor = Interceptor { chain ->
            val request = chain.request()
            val requiresAuth = request.tag(Invocation::class.java)
                ?.method()
                ?.getAnnotation(RequiresAuth::class.java) != null
            val methodName = request.tag(Invocation::class.java)
                ?.method()?.name ?: "null"

            Log.d("ApiService", "methodName: $methodName, requiresAuth: $requiresAuth")

            return@Interceptor if (requiresAuth) {
                val token = tokenHolder.token
                val requestHeaders = request.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(requestHeaders)
            } else {
                chain.proceed(request)
            }
        }
        clientBuilder.addInterceptor(authInterceptor)

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
        }

        val client = clientBuilder.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(ENDPOINT.toHttpUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}