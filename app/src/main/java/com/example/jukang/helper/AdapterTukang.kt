package com.example.jukang.helper

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jukang.data.response.TukangItem
import com.example.jukang.databinding.CardtukangBinding
import com.example.jukang.view.detailTukang.DetailActivity

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
    }
}