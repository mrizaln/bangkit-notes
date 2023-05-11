package com.dicoding.jetreward.model

import androidx.annotation.DrawableRes

data class Reward(
    val id: Long,
    @DrawableRes val image: Int,
    val title: String,
    val requiredPoint: Int,
)
