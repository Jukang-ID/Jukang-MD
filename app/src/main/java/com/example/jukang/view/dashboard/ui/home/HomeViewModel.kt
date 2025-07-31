package com.example.jukang.view.dashboard.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.TukangListItem
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _dataTukang = MutableLiveData<List<TukangListItem>?>()
    val dataTukang: LiveData<List<TukangListItem>?> get() = _dataTukang

    private val _loadingHome = MutableLiveData<Boolean>()
    val loadingHome: LiveData<Boolean> get() = _loadingHome

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _notifikasi = MutableLiveData<Boolean>()
    val notifikasi: LiveData<Boolean> get() = _notifikasi

    private val _isEmpety = MutableLiveData<Boolean>()
    val isEmpety: LiveData<Boolean> get() = _isEmpety

    var repository = HomeRepository(RetrofitClient)


    // Fungsi untuk mengambil data tukang
    fun fetchTukang(domisli: String) {
        _loadingHome.value = true
        viewModelScope.launch {
            try {
                val response = repository.getMainData(domisli)
                val dataTukang = response

                if(dataTukang.isEmpty()){
                    _isEmpety.value = true
                    _loadingHome.value = false
                }else{
                    _dataTukang.value = dataTukang as List<TukangListItem>?
                    _loadingHome.value = false
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _loadingHome.value = false
                _notifikasi.value = true
                _error.value = "Terjadi kesalahan dalam pengambilan data"
            }
        }
    }
}
