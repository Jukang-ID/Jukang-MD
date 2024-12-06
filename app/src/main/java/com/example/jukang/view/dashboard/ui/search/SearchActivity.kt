package com.example.jukang.view.dashboard.ui.search

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jukang.data.response.TukangItem
import com.example.jukang.databinding.ActivitySearchBinding
import com.example.jukang.helper.adapter.AdapterTukang
import com.example.jukang.helper.util.SearchUtil

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapterTukang: AdapterTukang
    private val tukangList: MutableList<TukangItem?> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tukangList.addAll(intent.getParcelableArrayListExtra("TUKANG_LIST") ?: emptyList())

        setupRecyclerView()
        setupSearchView()
    }

    private fun setupRecyclerView() {
        adapterTukang = AdapterTukang(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapterTukang
        adapterTukang.updateList(tukangList.filterNotNull())
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                updateSearchResults(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                updateSearchResults(newText)
                return false
            }
        })
    }

    private fun updateSearchResults(query: String?) {
        val filteredList = SearchUtil.filterTukangList(query, tukangList)
        adapterTukang.updateList(filteredList)

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Tidak ada layanan yang kamu cari", Toast.LENGTH_SHORT).show()
        }
    }
}
