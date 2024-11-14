package com.example.jukang.view.history.detailhistory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jukang.R
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.Tukang
import com.example.jukang.data.response.TukangReq
import com.example.jukang.databinding.ActivityDetailHistoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailHistory : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryBinding
    private var isRating = false
    private var Rating:Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkStatus()

        val idtukang = intent.getStringExtra(idtukangdetail)
        val namatukang = intent.getStringExtra(namatukangdetail)
        val tanggal = intent.getStringExtra(tanggaldetail)
        val metode = intent.getStringExtra(metode)
        val total = intent.getStringExtra(total)
        val spesialis = intent.getStringExtra(spesialis)

        binding.date.text = tanggal
        binding.idTukanng.text = idtukang
        binding.nama.text = namatukang
        binding.method.text = metode
        binding.totalStruk.text = total

        binding.selesaihistory.setOnClickListener {
            val rating = binding.ratingBar.rating
            if (rating != null) {
                Rating = rating
                updateTukang(namatukang.toString(), spesialis.toString(), rating.toString(), false, idtukang.toString())
            }

            Log.d("Detaial Activity", "onCreate: $rating")
        }


    }

    fun checkStatus () {
        binding.ratingBar.rating = Rating ?: 1f
    }


    fun updateTukang(nama: String, spesialis: String, review: String, rating: Boolean,id: String) {
        val req = TukangReq(
            nama,
            spesialis,
            review,
            rating
        )

        val call = RetrofitClient.Jukang.updateTukang(req, id)
        call.enqueue(object : Callback<Tukang> {
            override fun onResponse(call: Call<Tukang>, response: Response<Tukang>) {
//                jangan di isi ,otomatis ke onFailure
            }

            override fun onFailure(call: Call<Tukang>, t: Throwable) {
                Toast.makeText(this@DetailHistory, "Berhasil memberi rating", Toast.LENGTH_SHORT).show()
                finish()
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
    }
}