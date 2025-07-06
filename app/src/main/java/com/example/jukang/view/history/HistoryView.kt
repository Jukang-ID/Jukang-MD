package com.example.jukang.view.history

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.DataItem
import com.example.jukang.data.response.TransaksiData
import com.example.jukang.data.response.TransaksiItem
import com.example.jukang.helper.adapter.AdapterHistory
import kotlinx.coroutines.launch

class HistoryView:ViewModel() {

    private val _dataHistory = MutableLiveData<List<TransaksiItem>?>()
    val dataHistory: MutableLiveData<List<TransaksiItem>?> get() = _dataHistory

    private val _loadingHistory = MutableLiveData<Boolean>()
    val loadingHistory: MutableLiveData<Boolean> get() = _loadingHistory

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> get() = _error

    fun fetchDataHistory(id:String){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.Jukang.getTransaksiUser(id)
                val data = response.data

                _dataHistory.value = data as List<TransaksiItem>?
                _loadingHistory.value = false

            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("History", "fetchDataHistory: ${e.message}  ")
                _loadingHistory.value = true
                _error.value = "belum ada transaksi"
            }
        }
    }
}