package com.example.jukang.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val NEWS = "https://api-berita-indonesia.vercel.app/"
    private const val JUKANG = "https://jukang-api-177471570498.asia-southeast2.run.app/"

    val instance: Apiservice by lazy {
        Retrofit.Builder()
            .baseUrl(NEWS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Apiservice::class.java)
    }

    val Jukang: ApiService2 by lazy {
        Retrofit.Builder()
            .baseUrl(JUKANG)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService2::class.java)
    }
}