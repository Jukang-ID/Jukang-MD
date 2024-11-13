package com.example.jukang.view.dashboard.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.TukangItem
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _dataTukang = MutableLiveData<List<TukangItem>?>()
    val dataTukang: LiveData<List<TukangItem>?> get() = _dataTukang

    private val _loadingHome = MutableLiveData<Boolean>()
    val loadingHome: LiveData<Boolean> get() = _loadingHome

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchTukang(){
        _loadingHome.value =true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.Jukang.getTukang()
                val dataTukang = response.tukang?.filter { tukang ->
                    tukang?.booked == false // Memfilter tukang yang booked = false
                }

                if (dataTukang.isNullOrEmpty()){
                    _loadingHome.value = true
                    return@launch
                }
                _loadingHome.value = false
                _dataTukang.value = dataTukang as List<TukangItem>?

            }catch (e:Exception){
                e.printStackTrace()
                _loadingHome.value = true
                _error.value = "Failed to fetch data"
            }
        }
    }

}