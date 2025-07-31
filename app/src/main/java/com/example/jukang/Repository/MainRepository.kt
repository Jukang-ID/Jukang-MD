package com.example.jukang.Repository

import com.example.jukang.data.response.TukangListItem

interface MainRepository {
    suspend fun getMainData(domisili:String):List<TukangListItem>
}