package com.example.jukang.data

import com.example.jukang.data.response.NewsResponse
import retrofit2.http.GET

interface Apiservice {
    @GET("api/category/indonesia/society")
    suspend fun getNews(): NewsResponse
}