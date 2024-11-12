package com.example.jukang.data

import com.example.jukang.data.response.BeritaResponse
import com.example.jukang.data.response.Login
import com.example.jukang.data.response.Register
import com.example.jukang.data.response.loginRequest
import com.example.jukang.data.response.registerRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Apiservice {
    @GET("cnn/terbaru")
    suspend fun getNews(): BeritaResponse
}

interface ApiService2 {
    @POST("register")
    fun register(
        @Body registerReq : registerRequest
    ): Call<Register>

    @POST("users/login")
    fun login(
        @Body loginReq : loginRequest
    ): Call<Login>
}