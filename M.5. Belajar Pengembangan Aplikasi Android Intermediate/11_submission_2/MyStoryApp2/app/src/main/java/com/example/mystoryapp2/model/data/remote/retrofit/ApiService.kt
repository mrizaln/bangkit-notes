package com.example.mystoryapp2.model.data.remote.retrofit

import com.example.mystoryapp2.model.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class RequiresAuth


interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email")
        email: String,

        @Field("password")
        password: String,
    ): LoginResponse


    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name")
        name: String,

        @Field("email")
        email: String,

        @Field("password")
        password: String,
    ): RegisterResponse


    @RequiresAuth
    @GET("stories")
    suspend fun getStories(
        @Query("page")
        page: Int?,

        @Query("size")
        size: Int?,

        @Query("location")
        location: Int? = 0,       // possible values: 0, 1 (Boolean represented as Int)
    ): StoryListResponse


    @RequiresAuth
    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Path("id")
        id: String,
    ): StoryDetailResponse


    @RequiresAuth
    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part
        file: MultipartBody.Part,

        @Part("description")
        description: RequestBody,

        @Part("lat")
        lat: Float? = null,

        @Part("lon")
        lon: Float? = null,
    ): NewStoryResponse
}