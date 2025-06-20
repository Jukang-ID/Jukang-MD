package com.example.jukang.helper.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jukang.data.response.TukangItem
import com.example.jukang.data.response.TukangListItem
import com.example.jukang.databinding.CardtukangBinding
import com.example.jukang.view.detailTukang.DetailActivity
import com.example.jukang.view.payment.PaymentActivity

class AdapterTukang(private var listTukang: MutableList<TukangListItem>) : RecyclerView.Adapter<AdapterTukang.ViewHolder>() {

    class ViewHolder(val binding: CardtukangBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tukang: TukangListItem) {
            binding.NamaTukang.text = tukang.namatukang
            binding.Spesialis.text = tukang.spesialis
            binding.price.text = "${tukang.priceRupiah} / Hari"
            binding.rating.text = tukang.review
            binding.lokasi.text = tukang.domisili
            Glide.with(itemView.context)
                .load(tukang.photoUrl)
                .into(binding.photourls)
        }
    }

    // Update the list of tukang items and notify the adapter
    fun updateList(newList: List<TukangListItem>) {
        listTukang = newList.toMutableList()
        notifyDataSetChanged()  // Notify that the data has changed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardtukangBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.btnBooking.setOnClickListener {
            // Handle booking button click (if needed)
        }

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listTukang.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listTukang[position])

        holder.itemView.setOnClickListener {
            val id = listTukang[position].tukangId
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ID, id)
            holder.itemView.context.startActivity(intent)
        }

        holder.binding.btnBooking.setOnClickListener {
            val tukangItem = listTukang[position]
            val intent = Intent(holder.itemView.context, PaymentActivity::class.java).apply {
                putExtra(PaymentActivity.idTukang, tukangItem.tukangId)
                putExtra(PaymentActivity.namaTukang, tukangItem.namatukang)
                putExtra(PaymentActivity.spesialis, tukangItem.spesialis)
                putExtra(PaymentActivity.gambarTukang, tukangItem.photoUrl)
                putExtra(PaymentActivity.harga, tukangItem.priceRupiah)
                putExtra(PaymentActivity.rating, tukangItem.review)
                putExtra(PaymentActivity.booked, tukangItem.booked)
                putExtra(PaymentActivity.domisili, tukangItem.domisili)
            }
            holder.itemView.context.startActivity(intent)
        }
    }
}
