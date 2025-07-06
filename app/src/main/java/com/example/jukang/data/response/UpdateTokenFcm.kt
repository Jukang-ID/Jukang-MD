package com.example.jukang.data.response

import com.google.gson.annotations.SerializedName

data class UpdateTokenFcm(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class requestFcm(
    val token: String
)