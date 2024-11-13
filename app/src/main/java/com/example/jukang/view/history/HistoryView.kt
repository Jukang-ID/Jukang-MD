package com.example.jukang.view.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.DataItem
import com.example.jukang.helper.adapter.AdapterHistory
import kotlinx.coroutines.launch

class HistoryView:ViewModel() {

    private val _dataHistory = MutableLiveData<List<DataItem>?>()
    val dataHistory: MutableLiveData<List<DataItem>?> get() = _dataHistory

    private val _loadingHistory = MutableLiveData<Boolean>()
    val loadingHistory: MutableLiveData<Boolean> get() = _loadingHistory

    fun fetchDataHistory(id:String){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.Jukang.getTransaksi(id)
                val data = response.data

                _dataHistory.value = data as List<DataItem>?
                _loadingHistory.value = false

            } catch (e: Exception) {
                e.printStackTrace()
                _loadingHistory.value = true
            }
        }
    }
}