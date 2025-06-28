package com.example.jukang.view.tukang.ui.pesanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.TransaksiItem
import kotlinx.coroutines.launch

class PesananViewModel: ViewModel() {

    private val _data = MutableLiveData<List<TransaksiItem>?>()
    val data: LiveData<List<TransaksiItem>?> = _data

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    fun fetchDataApi(id_tukang: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.Jukang.getPEsananTukang(id_tukang)
                val filteredList = response.data?.filter { it?.statusCode == "pending" || it?.statusCode == "diterima" }
                _isLoading.value = false
                if(response.data?.isEmpty() == true){
                    _isEmpty.value = true
                }else{
                    _data.value = filteredList as List<TransaksiItem>?
                    _isEmpty.value = false
                }

            }catch (e:Exception){
                e.printStackTrace()
                _errorMessage.value = "Failed to fetch data. reason :${e.message}"
                _isLoading.value = false
                _isEmpty.value = true
            }
        }
    }

}