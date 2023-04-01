package com.example.mygithubapp2.data.remote.retrofit

import com.example.mygithubapp2.data.remote.response.UserDetailResponse
import com.example.mygithubapp2.data.remote.response.UserItemResponse
import com.example.mygithubapp2.data.remote.response.UserSearchResponse
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    suspend fun getUsers(
        @Query("q") query: String
    ) : UserSearchResponse

    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ) : UserDetailResponse

    @GET("users/{username}/followers")
    suspend fun getFollowers(
        @Path("username") username: String
    ) : List<UserItemResponse>

    @GET("users/{username}/following")
    suspend fun getFollowing(
        @Path("username") username: String
    ) : List<UserItemResponse>
}