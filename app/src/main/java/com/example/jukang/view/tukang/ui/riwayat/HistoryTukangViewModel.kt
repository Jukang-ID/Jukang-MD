package com.example.jukang.view.tukang.ui.riwayat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.TransaksiItem
import kotlinx.coroutines.launch

class HistoryTukangViewModel: ViewModel() {

    private val _data = MutableLiveData<List<TransaksiItem>?>()
    val data: LiveData<List<TransaksiItem>?> = _data

    fun fetch(id_tukang: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.Jukang.getPEsananTukang(id_tukang)
                _data.value = response.data as List<TransaksiItem>?
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}