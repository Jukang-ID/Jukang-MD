package com.example.jukang.view.dashboard.ui.home

import com.example.jukang.Repository.MainRepository
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.TukangListItem

class HomeRepository(private val client: RetrofitClient) : MainRepository {
    override suspend fun getMainData(domisili: String): List<TukangListItem> {
        val response = client.Jukang.getTukang(domisili,false)
        val data = response.tukang
        return  data as List<TukangListItem>
    }
}