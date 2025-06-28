package com.example.jukang.view.tukang.data

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
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
import com.example.jukang.helper.struk.StrukActivity
import com.example.jukang.view.history.detailhistory.DetailHistory
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
            binding.lokasi.text = if(pesanan.domisili == "") "Tidak Ada" else pesanan.domisili
            binding.price.text = pesanan.total
            binding.tag.text = pesanan.spesialis
            binding.deskripsiPerbaikkan.text = "Deskripsi : ${pesanan.deskripsi}"
            binding.status.text = pesanan.statusCode

            binding.BtnTestAccpet.text = when(pesanan.statusCode){
                "pending" -> "Konfirmasi"
                "diterima" -> "Lihat Detail"
                "ditolak" -> "ditolak"
                "Selesai" -> "selesai"
                else -> "pending"
            }

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

            binding.btnaccept.setOnClickListener{
//                Toast.makeText(itemView.context, pesanan.idTransaksi, Toast.LENGTH_SHORT).show()

                when(pesanan.statusCode){
                    "pending" -> {
                        updateConfirmation(pesanan.idTransaksi.toString(), "diterima")
                        binding.gridLayout3.visibility = View.VISIBLE
                        binding.linearLayout3.visibility = View.VISIBLE
                    }
                    "diterima" -> {
                        binding.gridLayout3.visibility = View.VISIBLE
                        binding.linearLayout3.visibility = View.VISIBLE
                    }
                    "ditolak" -> updateConfirmation(pesanan.idTransaksi.toString(), "ditolak")
                    "Selesai" -> {
                        val intent = Intent(itemView.context, DetailHistory::class.java)
                        intent.putExtra(DetailHistory.idtukangdetail, pesanan.tukangId)
                        intent.putExtra(DetailHistory.namatukangdetail, pesanan.namatukang)
                        intent.putExtra(DetailHistory.tanggaldetail, pesanan.createdAt)
                        intent.putExtra(DetailHistory.metode, pesanan.metodePembayaran)
                        intent.putExtra(DetailHistory.total, pesanan.total)
                        intent.putExtra(DetailHistory.spesialis, pesanan.spesialis)
                        intent.putExtra(DetailHistory.tanggalDibuat, pesanan.createdAt)
                        intent.putExtra(DetailHistory.idTRansaksksi, pesanan.idTransaksi)
                        itemView.context.startActivity(intent)
                    }
                }
            }

            binding.btnLokasi.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://www.google.com/maps/search/?api=1&query=${pesanan.alamat}")
                binding.root.context.startActivity(intent)
            }

            binding.jadwalValue.text = checkAndFormatDate(pesanan.tanggal.toString())
        }

        fun updateConfirmation(id_transaksi: String, status: String) {
            val req = UpdateStatusReq(
                id_transaksi = id_transaksi,
                status_code = status
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