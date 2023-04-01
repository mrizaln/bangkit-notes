package com.example.mygithubapp2.ui.detail

data class UserDetail(
    val username: String,
    val fullName: String,
    val numOfFollowers: Int,
    val numOfFollowing: Int,
    val avatarUrl: String,
)