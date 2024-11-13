package com.example.jukang.helper.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jukang.data.response.TukangItem
import com.example.jukang.databinding.CardtukangBinding
import com.example.jukang.view.detailTukang.DetailActivity
import com.example.jukang.view.payment.PaymentActivity

class AdapterTukang(private val listTukang: List<TukangItem>) :RecyclerView.Adapter<AdapterTukang.ViewHolder>() {

    class ViewHolder(val binding: CardtukangBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(tukang:TukangItem){
            binding.NamaTukang.text = tukang.namatukang
            binding.Spesialis.text = tukang.spesialis
            binding.price.text = tukang.priceRupiah
            binding.rating.text = tukang.review
            Glide.with(itemView.context)
                .load(tukang.photoUrl)

                .into(binding.photourls)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardtukangBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.btnBooking.setOnClickListener {

        }

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listTukang.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listTukang[position])

        holder.itemView.setOnClickListener{
            val id = listTukang[position].tukangId
            val intent = Intent(holder.itemView.context,DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ID,id)
            holder.itemView.context.startActivity(intent)
        }

        holder.binding.btnBooking.setOnClickListener {
            val id = listTukang[position].tukangId
            val nama = listTukang[position].namatukang
            val spesialis = listTukang[position].spesialis
            val gambar = listTukang[position].photoUrl
            val harga = listTukang[position].priceRupiah
            val rating = listTukang[position].review
            val booked = listTukang[position].booked

            val intent = Intent(holder.itemView.context,PaymentActivity::class.java)

            intent.putExtra(PaymentActivity.idTukang,id)
            intent.putExtra(PaymentActivity.namaTukang,nama)
            intent.putExtra(PaymentActivity.spesialis,spesialis)
            intent.putExtra(PaymentActivity.gambarTukang,gambar)
            intent.putExtra(PaymentActivity.harga,harga)
            intent.putExtra(PaymentActivity.rating,rating)
            intent.putExtra(PaymentActivity.booked,booked)


            holder.itemView.context.startActivity(intent)
        }
    }
}