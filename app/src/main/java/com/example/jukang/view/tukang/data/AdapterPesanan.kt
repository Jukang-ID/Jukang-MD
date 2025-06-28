package com.example.jukang.view.tukang.data

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Update
import com.bumptech.glide.Glide
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.TransaksiItem
import com.example.jukang.data.response.UpdateStatusReq
import com.example.jukang.data.response.UpdateStatusTransaksiResponse
import com.example.jukang.databinding.CardPesananBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdapterPesanan(private val listPesanan: MutableList<TransaksiItem>) :
    RecyclerView.Adapter<AdapterPesanan.viewHolder>() {
    class viewHolder(val binding: CardPesananBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(pesanan: TransaksiItem) {
            binding.NamaTukang.text = pesanan.namatukang
            binding.lokasi.text = pesanan.domisili
            binding.price.text = pesanan.total
            binding.tag.text = pesanan.spesialis
            binding.deskripsiPerbaikkan.text = "Deskripsi : ${pesanan.deskripsi}"
            binding.status.text = pesanan.statusCode

            binding.Alamat.text = "Alamat : ${pesanan.alamat}"

            Glide.with(itemView.context)
                .load(pesanan.photoprofile)
                .into(binding.photourls)

            binding.ulang.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data =
                    Uri.parse("https://wa.me/${formatPhoneNumberForWhatsApp(pesanan.nomorTelpon.toString())}")
                binding.root.context.startActivity(intent)
            }

            binding.jadwalValue.text = checkAndFormatDate(pesanan.tanggal.toString())
        }

        fun updateConfirmation(id_transaksi: String, status: String) {
            val req = UpdateStatusReq(
                id_transaksi = id_transaksi,
                Status_code = status
            )

            val call = RetrofitClient.Jukang.updateTransaksiStatus(req)
            call.enqueue(object : Callback<UpdateStatusTransaksiResponse> {
                override fun onResponse(
                    call: Call<UpdateStatusTransaksiResponse>,
                    response: Response<UpdateStatusTransaksiResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        binding.status.text = status
                        Toast.makeText(itemView.context, data?.message.toString(), Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<UpdateStatusTransaksiResponse>, t: Throwable) {
                    Toast.makeText(itemView.context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }

        fun formatPhoneNumberForWhatsApp(phoneNumber: String): String {
            // Check if the number starts with "0" and is not an empty string
            return if (phoneNumber.startsWith("0") && phoneNumber.isNotEmpty()) {
                "62" + phoneNumber.substring(1) // Replace the leading "0" with "62"
            } else {
                phoneNumber // Return the original number if it doesn't start with "0"
            }
        }

        fun checkAndFormatDate(dateString: String): String {
            // Define the date format
            val dateFormat = SimpleDateFormat("dd-M-yyyy", Locale.getDefault())

            val todayDateFormatted = dateFormat.format(Date())

            return if (dateString == todayDateFormatted) {
                "Hari ini"
            } else {
                dateString
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = CardPesananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listPesanan.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bind(listPesanan[position])

        holder.itemView.setOnClickListener {

        }
    }
}