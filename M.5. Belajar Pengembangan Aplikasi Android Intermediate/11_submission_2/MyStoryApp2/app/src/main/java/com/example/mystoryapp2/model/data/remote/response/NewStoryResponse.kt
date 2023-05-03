package com.example.mystoryapp2.model.data.remote.response

import com.google.gson.annotations.SerializedName

data class NewStoryResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,
)
