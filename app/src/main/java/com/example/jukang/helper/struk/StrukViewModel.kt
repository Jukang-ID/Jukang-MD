package com.example.jukang.helper.struk

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.TransaksiData
import com.example.jukang.data.response.TransaksiItem
import kotlinx.coroutines.launch

class StrukViewModel:ViewModel() {

    val _data = MutableLiveData<TransaksiItem>()
    val data: MutableLiveData<TransaksiItem> = _data

    val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> = _loading

    val _error = MutableLiveData<String>()
    val error: MutableLiveData<String> = _error

    fun fetchDatatransaksi(id:String){
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = RetrofitClient.Jukang.getTransaksibyId(id)
                if(response != null){
                    _data.value = response
                }else{
                    _error.value = "Data Tidak Ditemukan"
                }
                _loading.value = false
            }catch (e:Exception){
                e.printStackTrace()

            }
        }
    }

}