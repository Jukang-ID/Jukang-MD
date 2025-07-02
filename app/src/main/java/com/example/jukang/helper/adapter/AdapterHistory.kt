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
            binding.NamaTukangHistory.text = history.dataTukang?.namatukang
            binding.tanggalhist.text = history.tanggal
            binding.totalHistr.text = history.dataTukang?.priceRupiah
            binding.Method.text = history.metodePembayaran
            binding.idTukangHis.text = history.tukangId
            binding.spesialisHis.text = history.dataTukang?.spesialis
            binding.tanggalCreate.text = "tanggal dibuat : "+history.createdAt
            binding.statusCode.text = history.statusCode
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
            intent.putExtra(DetailHistory.idtukangdetail, listHistory[position].tukangId)
            intent.putExtra(DetailHistory.namatukangdetail, listHistory[position].dataTukang?.namatukang)
            intent.putExtra(DetailHistory.tanggaldetail, listHistory[position].createdAt)
            intent.putExtra(DetailHistory.metode, listHistory[position].metodePembayaran)
            intent.putExtra(DetailHistory.total, listHistory[position].dataTukang?.priceRupiah)
            intent.putExtra(DetailHistory.spesialis, listHistory[position].dataTukang?.spesialis)
            intent.putExtra(DetailHistory.tanggalDibuat, listHistory[position].createdAt)
            intent.putExtra(DetailHistory.idTRansaksksi, listHistory[position].idTransaksi)
            holder.itemView.context.startActivity(intent)
        }
    }
}