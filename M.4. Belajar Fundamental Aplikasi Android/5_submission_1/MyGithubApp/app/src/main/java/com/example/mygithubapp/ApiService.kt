package com.example.mygithubapp

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUsers(
        @Query("q") query: String
    ) : Call<UserSearchResponse>

    @GET("users/{username}")
    fun getUser(
        @Path("username") username: String
    ) : Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ) : Call<List<UserItemResponse>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ) : Call<List<UserItemResponse>>

}