package com.example.jukang.view.history.detailhistory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.TransaksiItem
import kotlinx.coroutines.launch

class DetailHistoryViewmodel : ViewModel() {

    private val dataTransaksi = MutableLiveData<TransaksiItem>()
    val data: MutableLiveData<TransaksiItem>
        get() = dataTransaksi

    private val loading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean>
        get() = loading

    private val error = MutableLiveData<String>()
    val isError: MutableLiveData<String>
        get() = error

    private val isEmpty = MutableLiveData<Boolean>()
    val isEmptyList: MutableLiveData<Boolean>
        get() = isEmpty

    fun fetchStruk(idTransaksi: String) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val res = RetrofitClient.Jukang.getTransaksiUser(id_transaksi = idTransaksi)
                val datas = res.data
                dataTransaksi.postValue(datas!![0])
                isEmpty.value = false
                isLoading.value = false
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

}