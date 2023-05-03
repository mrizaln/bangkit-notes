package com.example.mystoryapp2.model.data.remote.retrofit

import android.util.Log
import com.example.mystoryapp2.BuildConfig
import com.example.mystoryapp2.model.di.TokenHolder
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
