package com.example.jukang.view.history

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jukang.R
import com.example.jukang.data.RetrofitClient
import com.example.jukang.databinding.ActivityHistoryBinding
import com.example.jukang.helper.adapter.AdapterHistory
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel : HistoryView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.error.visibility = View.GONE
        binding.cat.visibility = View.GONE

        viewModel = HistoryView()

        viewModel.loadingHistory.observe(this, {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        viewModel.error.observe(this,{ errpr ->
            if(errpr != null){
                binding.progressBar.visibility = View.GONE
                binding.error.visibility = View.VISIBLE
                binding.error.text = errpr
                binding.cat.visibility = View.VISIBLE
            }
        })

        viewModel.dataHistory.observe(this, {
            val adapter = it?.let { it1 -> AdapterHistory(it1) }
            binding.listRiwayat.adapter = adapter
        })

        binding.btnBAckPay.setOnClickListener {
            finish()
        }


        binding.listRiwayat.layoutManager = LinearLayoutManager(this)
        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val id = pref.getString("UID", null)

        viewModel.fetchDataHistory(id.toString())


    }


}