package com.example.jukang.view.dashboard.ui.search

import android.os.Bundle
import android.view.View
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

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Back button logic
        binding.backButton.setOnClickListener {
            finish() // Close the activity
        }

        // Search View listener
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText.toString())
                return true
            }
        })
    }

    private fun search(keyword: String) {
        // Show loading
        binding.progressBar.visibility = View.VISIBLE

        val request = requestTukang(keyword)
        val call = RetrofitClient.Jukang.search(request)

        call.enqueue(object : Callback<TukangDomisili> {
            override fun onResponse(call: Call<TukangDomisili>, response: Response<TukangDomisili>) {
                if (response.isSuccessful) {
                    val tukang = response.body()
                    val data = tukang?.tukangList?.filterNotNull()
                    if (!data.isNullOrEmpty()) {
                        adapterTukang = AdapterTukang(data)
                        binding.recyclerView.adapter = adapterTukang
                    } else {
                        Toast.makeText(this@SearchActivity, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SearchActivity, "Response tidak berhasil", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<TukangDomisili>, t: Throwable) {
                // Hide loading
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@SearchActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
