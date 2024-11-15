package com.example.jukang.view.history.detailhistory

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.Room.Database
import com.example.jukang.data.Room.historyRiwayat
import com.example.jukang.data.Room.historycheckDAO
import com.example.jukang.data.response.Data
import com.example.jukang.data.response.Tukang
import com.example.jukang.data.response.TukangReq
import com.example.jukang.databinding.ActivityDetailHistoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailHistory : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryBinding
    private lateinit var db: Database
    private lateinit var history: historycheckDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val idtukang = intent.getStringExtra(idtukangdetail)
        val namatukang = intent.getStringExtra(namatukangdetail)
        val tanggal = intent.getStringExtra(tanggaldetail)
        val metode = intent.getStringExtra(metode)
        val total = intent.getStringExtra(total)
        val spesialis = intent.getStringExtra(spesialis)
        val tanggalDibuat = intent.getStringExtra(tanggalDibuat)

        val preferences = getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val namauser = preferences.getString("NAME", "")

        binding.date.text = tanggal
        binding.idTukanng.text = idtukang
        binding.nama.text = namatukang
        binding.method.text = metode
        binding.totalStruk.text = total

        db = Database.getDatabase(this)
        history = db.historyDao()

        binding.backdet.setOnClickListener {
            finish()
        }


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = history.checkHistory(namauser.toString(), tanggalDibuat.toString())
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
                saveStatus(tanggalDibuat.toString(), namauser.toString())
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

    companion object {
        const val idtukangdetail = "idtukang"
        const val namatukangdetail = "nama"
        const val tanggaldetail = "tanggal"
        const val metode = "metode"
        const val total = "total"
        const val spesialis = "rating"
        const val tanggalDibuat = "tanggal dibuat"
    }
}