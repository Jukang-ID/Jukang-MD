package com.example.jukang.data

import com.example.jukang.data.response.BeritaResponse
import retrofit2.http.GET

interface Apiservice {
    @GET("cnn/terbaru")
    suspend fun getNews(): BeritaResponse
}