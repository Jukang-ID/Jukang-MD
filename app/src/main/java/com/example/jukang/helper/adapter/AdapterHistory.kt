package com.example.jukang.helper.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jukang.data.response.DataItem
import com.example.jukang.databinding.CardhistoryBinding
import com.example.jukang.view.history.detailhistory.DetailHistory

class AdapterHistory(private val listHistory: List<DataItem>) :RecyclerView.Adapter<AdapterHistory.ViewHolder>() {
    class ViewHolder(val binding: CardhistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(history:DataItem){
            binding.NamaTukangHistory.text = history.namatukang
            binding.tanggalhist.text = history.tanggal
            binding.totalHistr.text = history.total
            binding.Method.text = history.metodePembayaran
            binding.idTukangHis.text = history.idTukang
            binding.spesialisHis.text = history.spesialis
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

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailHistory::class.java)
            intent.putExtra(DetailHistory.idtukangdetail, listHistory[position].idTukang)
            intent.putExtra(DetailHistory.namatukangdetail, listHistory[position].namatukang)
            intent.putExtra(DetailHistory.tanggaldetail, listHistory[position].createdAt)
            intent.putExtra(DetailHistory.metode, listHistory[position].metodePembayaran)
            intent.putExtra(DetailHistory.total, listHistory[position].total)
            intent.putExtra(DetailHistory.spesialis, listHistory[position].spesialis)
            holder.itemView.context.startActivity(intent)
        }
    }
}