package com.example.jukang.helper.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jukang.data.response.DataItem
import com.example.jukang.data.response.TransaksiItem
import com.example.jukang.databinding.CardhistoryBinding
import com.example.jukang.view.history.detailhistory.DetailHistory

class AdapterHistory(private val listHistory: List<TransaksiItem>) :RecyclerView.Adapter<AdapterHistory.ViewHolder>() {
    class ViewHolder(val binding: CardhistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(history:TransaksiItem){
            binding.namaTukang.text = history.dataTukang?.namatukang
            binding.totalBiaya.text = history.dataTukang?.priceRupiah
//            binding.Method.text = history.metodePembayaran
            binding.idTransaksi.text = "ID : ${history.idTransaksi}"
            binding.Spesialis.text = history.dataTukang?.spesialis
            binding.status.text = history.statusCode

            Glide.with(binding.root)
                .load(history.dataTukang?.photoUrl)
                .into(binding.imageView2)


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