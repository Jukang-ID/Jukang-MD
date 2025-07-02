package com.example.jukang.view.tukang.ui.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jukang.data.RetrofitClient
import kotlinx.coroutines.launch

class DashboardViewmodel:ViewModel() {

    private val _transaksi = MutableLiveData<Int?>()
    val transaksi: MutableLiveData<Int?> = _transaksi

    private val _detailTukang = MutableLiveData<String?>()
    val detailTukang: MutableLiveData<String?> = _detailTukang

    fun fetch (id:String){
        viewModelScope.launch {
            try {
                val responseTransaksi = RetrofitClient.Jukang.getPEsananTukang(id)
                val responseDetailTukang = RetrofitClient.Jukang.getDetailTukang(id)
                _transaksi.value = responseTransaksi.data?.size ?: 0
                _detailTukang.value = responseDetailTukang.detailTukang?.review.toString() ?: "0"
                Log.d("Dashboard", "fetch: ${responseDetailTukang}")
            }catch (e:Exception){
                Log.d("Dashboard", "fetch: ${e.message}")

            }
        }
    }


}