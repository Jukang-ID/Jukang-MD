package com.example.jukang.view.payment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.jukang.R
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.Payment
import com.example.jukang.data.response.Tukang
import com.example.jukang.data.response.TukangReq
import com.example.jukang.data.response.paymentReq
import com.example.jukang.databinding.ActivityPaymentBinding
import com.example.jukang.helper.struk.StrukActivity
import com.example.jukang.view.dashboard.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Tag
import java.util.Calendar
import kotlin.math.log

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar2.visibility = View.GONE

        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val id = pref.getString("UID", "").toString()
        val nama = pref.getString("NAME", "").toString()
        val namatukang = intent.getStringExtra(namaTukang).toString()
        val harga = intent.getStringExtra(harga).toString()
        val spesialis = intent.getStringExtra(spesialis).toString()
        val gambar = intent.getStringExtra(gambarTukang).toString()
        val tunai = binding.tunai.text.toString()
        val status = intent.getBooleanExtra(booked, false)
        val rating = intent.getStringExtra(rating).toString()
        val idTukang = intent.getStringExtra(idTukang).toString()



        Glide.with(this@PaymentActivity)
            .load(gambar)
            .into(binding.pototukang)

        binding.tukangNm.text = namatukang
        binding.harga.text = harga
        binding.spesialisTk.text = spesialis

        binding.btnBAckPay.setOnClickListener {
            finish()
        }

        binding.tanggal.setOnClickListener{
            showDatePickerDialog()
        }

        binding.btnPayment.setOnClickListener {
            binding.progressBar2.visibility = View.VISIBLE
            val deskripsi = binding.deskripsiPerbaikan.text.toString().trim()
            val tanggal = binding.tanggal.text.toString().trim()
            val alamat = binding.Alamat.text.toString().trim()

            Log.d(Tag, "onCreate: $status")
            update(namatukang, spesialis, rating, status,idTukang)
            paymentMethod(id, nama, namatukang,idTukang ,spesialis, deskripsi, tanggal, alamat, tunai, harga)
        }

//        checkStatus()


    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                binding.tanggal.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun paymentMethod(
        user_id: String,
        namalengkap: String,
        namatukang: String,
        id_tukang: String,
        spesialis: String,
        deskripsi: String,
        tanggal: String,
        alamat: String,
        metodePembayaran: String,
        total: String
    ) {
        val request = paymentReq(
            user_id,
            namalengkap,
            namatukang,
            id_tukang,
            spesialis,
            deskripsi,
            tanggal,
            alamat,
            metodePembayaran,
            total
        )

        val call = RetrofitClient.Jukang.addtransaksi(request)

        call.enqueue(object : Callback<Payment> {
            override fun onResponse(call: Call<Payment>, response: Response<Payment>) {
                if (response.isSuccessful){
                    binding.progressBar2.visibility = View.GONE
                    Toast.makeText(this@PaymentActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@PaymentActivity, StrukActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra(StrukActivity.namatukangs, namatukang)
                    intent.putExtra(StrukActivity.method, metodePembayaran)
                    intent.putExtra(StrukActivity.total, total)
                    intent.putExtra(StrukActivity.tanggal, tanggal)
                    intent.putExtra(StrukActivity.id, id_tukang)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@PaymentActivity, "Gagal : ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Payment>, t: Throwable) {
                Toast.makeText(this@PaymentActivity, "Gagal : ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("TAG", "onFailure: ${t.localizedMessage}")
            }
        })
    }

    private fun update(namatukang: String,spesialis: String,review: String,booked: Boolean,id: String){
        val requestUpdate = TukangReq(
            namatukang,
            spesialis,
            review,
            booked = true
        )
        val call = RetrofitClient.Jukang.updateTukang(requestUpdate, id)
        call.enqueue(object : Callback<Tukang> {
            override fun onResponse(call: Call<Tukang>, response: Response<Tukang>) {
                if (response.isSuccessful){
                    Log.d(Tag, "onResponse: ${response.body()}")
                } else {
                    Log.d(Tag, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Tukang>, t: Throwable) {

            }
        })

    }

    fun checkStatus (){
        if (binding.tukangNm.text.toString().isEmpty()){
            binding.tukangNm.error = "Nama Tukang tidak boleh kosong"
            finish()
        }
    }


    companion object {
        const val namaTukang = "namaTukang"
        const val harga = "harga"
        const val spesialis = "spesialis"
        const val gambarTukang = "https://i.pravatar.cc/300"
        const val Tag = "PaymentActivity"
        const val rating = "rating"
        const val booked = "status"
        const val idTukang = "id"
    }
}