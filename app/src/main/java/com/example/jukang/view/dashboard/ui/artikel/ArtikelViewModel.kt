package com.example.jukang.view.dashboard.ui.artikel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.BeritaResponse
import com.example.jukang.data.response.PostsItem
import kotlinx.coroutines.launch
import kotlin.math.log

class ArtikelViewModel : ViewModel() {

    private val _data = MutableLiveData<List<PostsItem>?>()
    val data: LiveData<List<PostsItem>?> get() = _data

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchData() {
        _loading.value = true
        viewModelScope.launch {
            try {
                // Panggil API dalam coroutine
                val response = RetrofitClient.instance.getNews()
                val datapost = response.data?.posts
                Log.d("Artikel", "fetchData: response $response")
                Log.d("Artikel", "fetchData: datapost $datapost")
                _loading.value = false
                _data.value = datapost as List<PostsItem>?


            } catch (e: Exception) {
                e.printStackTrace()
                _loading.value = true
                _error.value = "Failed to fetch data"


            }
        }
    }

}