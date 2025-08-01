package com.example.jukang.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val NEWS = "https://api-berita-indonesia.vercel.app/"
    private const val JUKANG = "https://api-jukang.vercel.app/"
    private const val MAPS = "https://api.openrouteservice.org/"

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
    val maps: ApiService3 by lazy {
        Retrofit.Builder()
            .baseUrl(MAPS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService3::class.java)
    }
}