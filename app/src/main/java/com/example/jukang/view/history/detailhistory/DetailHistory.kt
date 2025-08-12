package com.example.jukang.view.history.detailhistory

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.Room.Database
import com.example.jukang.data.Room.historyRiwayat
import com.example.jukang.data.Room.historycheckDAO
import com.example.jukang.data.response.Tukang
import com.example.jukang.data.response.TukangReq
import com.example.jukang.data.response.UpdateStatusReq
import com.example.jukang.data.response.UpdateStatusTransaksiResponse
import com.example.jukang.databinding.ActivityDetailHistoryBinding
import com.example.jukang.helper.utils.parseFormatWaltu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.droidsonroids.gif.GifImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.toString


class DetailHistory : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryBinding
    private lateinit var db: Database
    private lateinit var history: historycheckDAO
    private lateinit var viewmodel: DetailHistoryViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }
        viewmodel = ViewModelProvider(this).get(DetailHistoryViewmodel::class)

        db = Database.getDatabase(this)
        history = db.historyDao()

        val idtukang = intent.getStringExtra(idtukangdetail)
        val namatukang = intent.getStringExtra(namatukangdetail)
        val tanggal = intent.getStringExtra(tanggaldetail)
        val metode = intent.getStringExtra(metode)
        val total = intent.getStringExtra(total)
        val spesialis = intent.getStringExtra(spesialis)
        val tanggalDibuat = intent.getStringExtra(tanggalDibuat)
        val idTRansaksksis = intent.getStringExtra(idTRansaksksi)

        viewmodel.fetchStruk(idTRansaksksis.toString())

        viewmodel.isLoading.observe(this){loading ->
            if(loading){
                binding.Struk.visibility = View.GONE
            }else{
                binding.Struk.visibility = View.VISIBLE
            }
        }

        viewmodel.data.observe(this) { struk ->
            binding.totalBiaya.text = struk.dataTukang?.priceRupiah
            binding.idTransaksi.text = struk.idTransaksi
            binding.namaTukang.text = struk.dataTukang?.namatukang
            binding.Spesialis.text = struk.dataTukang?.spesialis
            binding.textView55.text = struk.dataTukang?.domisili

            binding.tanggal.text = parseFormatWaltu(struk.createdAt.toString())
            binding.namaUser.text = struk.dataUser?.namalengkap
            binding.nomorTelpon.text = struk.dataUser?.nomortelp
            binding.Alamat.text = struk.alamat

            binding.textView61.text = struk.deskripsi
            binding.metode.text = struk.metodePembayaran
            binding.SubTotal.text = struk.dataTukang?.priceRupiah

            binding.textView64.text = struk.dataTukang?.priceRupiah

            Glide.with(this)
                .load(struk.dataTukang?.photoUrl)
                .into(binding.imageView9)

        }

        val preferences = getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val namauser = preferences.getString("NAME", "")

        checkRating(namauser.toString(), tanggalDibuat.toString())

        binding.selesaihistory.setOnClickListener {
            val rating = binding.ratingBar.rating
            if (rating != null) {
                updateTukang(
                    namatukang.toString(),
                    spesialis.toString(),
                    rating.toString(),
                    false,
                    idtukang.toString()
                )
                updateConfirmation(idTRansaksksis.toString(), "Selesai")
                saveStatus(tanggalDibuat.toString(), namauser.toString())
            }
        }
    }

    fun checkRating(namaUser: String, tanggal: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = history.checkHistory(namaUser.toString(), tanggal.toString())
                withContext(Dispatchers.Main) {
                    if (data != null) {
                        binding.ratingBar.isEnabled = false
                        binding.selesaihistory.isEnabled = false
                    }
                }
            } catch (e: Exception) {
                Log.e("DetailHistory", "Error while checking history: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailHistory, "Error checking history: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun saveStatus(tanggal: String, namaUser: String) {
        db = Database.getDatabase(this)
        history = db.historyDao()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = history.checkHistory(namaUser, tanggal)
                if (data == null) {
                    history.inserhistory(
                        historyRiwayat(0, namaUser, tanggal)
                    )
                }
            } catch (e: Exception) {
                Log.e("DetailHistory", "Error while saving status: ${e.message}")
            }
        }
    }

    fun updateTukang(
        nama: String,
        spesialis: String,
        review: String,
        rating: Boolean,
        id: String,
    ) {
        val req = TukangReq(
            nama,
            spesialis,
            review,
            rating
        )

        val call = RetrofitClient.Jukang.updateTukang(req, id)
        call.enqueue(object : Callback<Tukang> {
            override fun onResponse(call: Call<Tukang>, response: Response<Tukang>) {

            }

            override fun onFailure(call: Call<Tukang>, t: Throwable) {
                Toast.makeText(this@DetailHistory, "Berhasil memberi rating", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }

        })

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
//                    binding.status.text = status
//                    Toast.makeText(itemView.context, data?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateStatusTransaksiResponse>, t: Throwable) {
                Toast.makeText(this@DetailHistory, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    companion object {
        const val idtukangdetail = "idtukang"
        const val namatukangdetail = "nama"
        const val tanggaldetail = "tanggal"
        const val metode = "metode"
        const val total = "total"
        const val spesialis = "rating"
        const val tanggalDibuat = "tanggal dibuat"
        const val idTRansaksksi = "id transaksi"
    }
}