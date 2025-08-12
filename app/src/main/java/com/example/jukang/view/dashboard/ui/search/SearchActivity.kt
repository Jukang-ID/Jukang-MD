package com.example.jukang.view.dashboard.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.TukangDomisili
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }


        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Back button logic
        binding.backButton.setOnClickListener {
            finish() // Close the activity
        }

        // Fokuskan pada SearchView dan tampilkan keyboard saat activity dimulai
        binding.searchView.requestFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT)

        binding.emptyPlaceholder.visibility = View.VISIBLE // Menampilkan placeholder "Mau cari tukang apa?"

        val setQueries = intent.getStringExtra(query)
        if (setQueries != null) {
            binding.searchView.setQuery(setQueries, false)
            search(setQueries)
        }


        // Search View listener
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Pencarian hanya akan dilakukan saat pengguna menekan Enter
                search(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Tidak melakukan pencarian saat text berubah, hanya di onQueryTextSubmit
                return false
            }
        })
    }

    companion object{
        const val query = "query"
    }

    // Pada metode search()
    private fun search(keyword: String) {
        // Menyembunyikan placeholder "Mau cari tukang apa?"
        // Menampilkan LoadingBiggy
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.emptyPlaceholder.visibility = View.GONE

        val request = requestTukang(keyword)
        val call = RetrofitClient.Jukang.search(request)

        call.enqueue(object : Callback<TukangDomisili> {
            override fun onResponse(call: Call<TukangDomisili>, response: Response<TukangDomisili>) {
                // Menyembunyikan LoadingBiggy
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val tukang = response.body()
                    val data = tukang?.tukangList?.filterNotNull()

                    Log.d("Search", "onResponse: ${tukang}")

                    if (!data.isNullOrEmpty()) {
                        // Menampilkan RecyclerView jika ada data
                        binding.emptyPlaceholder.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        adapterTukang = AdapterTukang(data as MutableList<TukangListItem>)
                        binding.recyclerView.adapter = adapterTukang
                    } else {
                        // Menampilkan placeholder jika data kosong
                        binding.emptyPlaceholder.text = "Data tidak ditemukan"
                        binding.emptyPlaceholder.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(this@SearchActivity, "Response tidak berhasil", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TukangDomisili>, t: Throwable) {
                // Menyembunyikan LoadingBiggy
                Log.d("Search", "onFailure: ${t.message}")
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.emptyPlaceholder.visibility = View.VISIBLE // Menampilkan placeholder jika gagal
                Toast.makeText(this@SearchActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}

