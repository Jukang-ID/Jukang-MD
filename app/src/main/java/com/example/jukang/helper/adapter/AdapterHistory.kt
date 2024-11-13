package com.example.jukang.helper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jukang.data.response.DataItem
import com.example.jukang.databinding.CardhistoryBinding

class AdapterHistory(private val listHistory: List<DataItem>) :RecyclerView.Adapter<AdapterHistory.ViewHolder>() {
    class ViewHolder(val binding: CardhistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(history:DataItem){
            binding.NamaTukangHistory.text = history.namatukang
            binding.tanggalhist.text = history.tanggal
            binding.totalHistr.text = history.total
            binding.Method.text = history.metodePembayaran
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardhistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listHistory[position])
    }
}