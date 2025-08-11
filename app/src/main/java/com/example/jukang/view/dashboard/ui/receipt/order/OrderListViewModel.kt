package com.example.jukang.view.dashboard.ui.receipt.order

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.TransaksiItem
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class OrderListViewModel : ViewModel() {


    private val _dataTransaksi = MutableLiveData<List<TransaksiItem>>()
    val dataTransaksi: MutableLiveData<List<TransaksiItem>> = _dataTransaksi

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> = _loading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: MutableLiveData<Boolean> = _isEmpty

    fun fetchData(id:String,status:String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.Jukang.getTransaksiUser(id,status)
                val data = response.data
                if(data?.isNotEmpty() ?: emptyList<TransaksiItem>().isNotEmpty()){
                    _dataTransaksi.value = response.data as List<TransaksiItem>?
                }else{
                    _isEmpty.value = true
                }
                Log.d(TAG, "fetchData: ${data?.size}")
                _loading.value = false
            }catch (e:Exception){
                e.printStackTrace()
                _loading.value = false
                _error.value = "belum ada transaksi"

            }
        }
    }

    companion object {
        private const val TAG = "OrderListViewModel"
    }

}