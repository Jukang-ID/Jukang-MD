package com.example.jukang.view.tukang.ui.riwayat

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jukang.R
import com.example.jukang.data.response.TransaksiItem
import com.example.jukang.databinding.ActivityHistoryTukangBinding
import com.example.jukang.view.tukang.data.AdapterPesanan

class HistoryTukangActivity : AppCompatActivity() {

    private lateinit var binding:ActivityHistoryTukangBinding

    private lateinit var viewModel: HistoryTukangViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryTukangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = HistoryTukangViewModel()

        binding.back.setOnClickListener {
            finish()
        }

        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val uid = pref.getString("UID", null)


        viewModel.fetch(uid.toString())

        viewModel.data.observe(this, Observer {
            binding.listPesanan.layoutManager = LinearLayoutManager(this)
            binding.listPesanan.adapter = AdapterPesanan(it as MutableList<TransaksiItem>)
        })

    }


}