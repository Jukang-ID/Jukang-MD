package com.example.jukang.view.dashboard.ui.search

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.TukangDomisili
import com.example.jukang.data.response.TukangItem
import com.example.jukang.data.response.TukangListItem
import com.example.jukang.data.response.requestTukang
import com.example.jukang.databinding.ActivitySearchBinding
import com.example.jukang.helper.adapter.AdapterTukang
import com.example.jukang.helper.util.SearchUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapterTukang: AdapterTukang
    private val tukangList: MutableList<TukangItem?> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })



    }


    fun search(keyword: String){
        val request = requestTukang(keyword)
        val call = RetrofitClient.Jukang.search(request)

        call.enqueue(object : Callback<TukangDomisili> {
            override fun onResponse(call: Call<TukangDomisili>, response: Response<TukangDomisili>) {
                if (response.isSuccessful) {
                    val tukang = response.body()
                    val data = tukang?.tukangList
                    if (tukang != null) {
                        val adapter = AdapterTukang(data as List<TukangListItem>)
                        binding.recyclerView.adapter = adapter
                    }else{
                        Toast.makeText(this@SearchActivity, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<TukangDomisili>, t: Throwable) {
                Toast.makeText(this@SearchActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        })
    }

//    private fun updateSearchResults(query: String?) {
//        val filteredList = SearchUtil.filterTukangList(query, tukangList)
//        adapterTukang.updateList(filteredList)
//
//        if (filteredList.isEmpty()) {
//            Toast.makeText(this, "Tidak ada layanan yang kamu cari", Toast.LENGTH_SHORT).show()
//        }
//    }
}
