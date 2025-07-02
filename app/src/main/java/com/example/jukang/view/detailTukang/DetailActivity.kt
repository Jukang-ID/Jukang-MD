package com.example.jukang.view.detailTukang

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.jukang.R
import com.example.jukang.data.RetrofitClient
import com.example.jukang.databinding.ActivityDetailBinding
import com.example.jukang.view.payment.PaymentActivity
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding
    private var photoUrl:String? = null
    private var status:Boolean = false
    private var idTukang:String? = null
    var priceNominal : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btnBAckPay.setOnClickListener {
            finish()
        }

        val id = intent.getStringExtra(EXTRA_ID)
        fetchDetail(id.toString())


    }

    fun fetchDetail(id:String){
        binding.loadingScreen.visibility = View.VISIBLE
        lifecycleScope.launch {
            val response = RetrofitClient.Jukang.getDetailTukang(id)
            if(response.status == "success"){
                binding.loadingScreen.visibility = View.GONE
                binding.containerBooking.visibility = View.VISIBLE
                binding.constraintLayout5.visibility = View.VISIBLE


                binding.namaTukang.text = response.detailTukang?.namatukang
                binding.Spesialis.text = response.detailTukang?.spesialis
                binding.harga.text = response.detailTukang?.priceRupiah
                binding.rating.text = response.detailTukang?.review.toString()
                binding.lokasi.text = response.detailTukang?.domisili
                binding.deskripsi.text = response.detailTukang?.deskripsi

                binding.senin.text = response.detailTukang?.jadwal?.senin
                binding.selasa.text = response.detailTukang?.jadwal?.selasa
                binding.rabu.text = response.detailTukang?.jadwal?.rabu
                binding.Kamis.text = response.detailTukang?.jadwal?.kamis
                binding.Jumat.text = response.detailTukang?.jadwal?.jumat
                binding.Sabtu.text = response.detailTukang?.jadwal?.sabtu
                binding.Minggu.text = response.detailTukang?.jadwal?.minggu


                priceNominal = response.detailTukang?.price.toString()
//                Toast.makeText(this@DetailActivity, "${priceNominal}", Toast.LENGTH_SHORT).show()


                photoUrl = response.detailTukang?.photoUrl
                status = response.detailTukang?.booked == true
                idTukang = response.detailTukang?.tukangId
                Glide.with(this@DetailActivity)
                    .load(response.detailTukang?.photoUrl)
                    .into(binding.photodetial)

                Glide.with(this@DetailActivity)
                    .load(response.detailTukang?.photoUrl)
                    .circleCrop()
                    .into(binding.photoPp)


                binding.status.text = if(response.detailTukang?.booked == false) "Tersedia" else "Tidak Tersedia"

                binding.btnBooking.setOnClickListener {
                    val intent = Intent(this@DetailActivity, PaymentActivity::class.java)
                    intent.putExtra(PaymentActivity.namaTukang, binding.namaTukang.text.toString())
                    intent.putExtra(PaymentActivity.harga, binding.harga.text.toString())
                    intent.putExtra(PaymentActivity.spesialis, binding.Spesialis.text.toString())
                    intent.putExtra(PaymentActivity.gambarTukang,photoUrl)
                    intent.putExtra(PaymentActivity.booked,status)
                    intent.putExtra(PaymentActivity.rating, binding.rating.text.toString())
                    intent.putExtra(PaymentActivity.idTukang,idTukang )
                    intent.putExtra(PaymentActivity.domisili, binding.lokasi.text.toString())
                    intent.putExtra(PaymentActivity.amount, priceNominal)
                    startActivity(intent)
                }

            }
        }
    }

    companion object{
        const val EXTRA_ID = "extra_id"
    }
}