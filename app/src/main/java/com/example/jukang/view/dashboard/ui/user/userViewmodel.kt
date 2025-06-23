package com.example.jukang.view.dashboard.ui.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.DataItem
import com.example.jukang.data.response.TukangListItem
import kotlinx.coroutines.launch

class userViewmodel : ViewModel() {
    private val _dataTukang = MutableLiveData<Int?>()
    val dataTukang: LiveData<Int?> get() = _dataTukang



    fun fetchTukangTransaksi (user_id:String){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.Jukang.getTransaksi(user_id)
                val data = response.data

                _dataTukang.value = data?.size
                Log.d("transaksi user", "${data?.size}")

            }catch (e:Exception){
                e.printStackTrace()
                Log.d("transaksi user", "${e.message}")
            }
        }
    }
}