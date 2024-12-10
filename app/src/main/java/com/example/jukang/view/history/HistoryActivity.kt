package com.example.jukang.view.history

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jukang.R
import com.example.jukang.databinding.ActivityHistoryBinding
import com.example.jukang.helper.adapter.AdapterHistory
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: HistoryView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout with View Binding
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = HistoryView()

        // Hide error and placeholder by default
        binding.error.visibility = View.GONE
        binding.cat.visibility = View.GONE

        // Correctly initialize GifImageView using View Binding
//        val gifImageView: GifImageView = binding.gifImageView

        // Set the GIF resource to the GifImageView
//        gifImageView.setImageResource(R.drawable.creditcard)

        // Observe loading state
        viewModel.loadingHistory.observe(this, { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        // Observe error state
        viewModel.error.observe(this, { error ->
            if (error != null) {
                binding.progressBar.visibility = View.GONE
                binding.error.visibility = View.VISIBLE
                binding.error.text = error
                binding.cat.visibility = View.VISIBLE
            }
        })

        // Observe data history
        viewModel.dataHistory.observe(this, { historyData ->
            val adapter = historyData?.let { AdapterHistory(it) }
            binding.listRiwayat.adapter = adapter
        })

        // Set up RecyclerView
        binding.listRiwayat.layoutManager = LinearLayoutManager(this)

        // Fetch UID from SharedPreferences
        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val id = pref.getString("UID", null)

        // Fetch data history
        viewModel.fetchDataHistory(id.toString())

        // Set back button listener
        binding.btnBAckPay.setOnClickListener {
            finish()  // Close the activity
        }
    }
}