package com.example.jukang.view.detailTukang

import android.content.Intent
import android.os.Bundle
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btnBAckPay.setOnClickListener {
            finish()
        }

        binding.btnbookDet.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra(PaymentActivity.namaTukang, binding.nmTukang.text.toString())
            intent.putExtra(PaymentActivity.harga, binding.pricedetail.text.toString())
            intent.putExtra(PaymentActivity.spesialis, binding.spesialisDetail.text.toString())
            intent.putExtra(PaymentActivity.gambarTukang,photoUrl)
            intent.putExtra(PaymentActivity.booked,status)
            intent.putExtra(PaymentActivity.rating, binding.ratingdetail.text.toString())
            intent.putExtra(PaymentActivity.idTukang,idTukang )
            intent.putExtra(PaymentActivity.domisili, binding.PosisiDetail.text.toString())
            startActivity(intent)
        }

        val id = intent.getStringExtra(EXTRA_ID)
        if(id != null){
            fetchDetail(id)
        }


    }

    fun fetchDetail(id:String){
        binding.progressBar2.visibility = View.VISIBLE
        lifecycleScope.launch {
            val response = RetrofitClient.Jukang.getDetailTukang(id)
            if(response.status == "success"){
                binding.progressBar2.visibility = View.GONE
                binding.nmTukang.text = response.detailTukang?.namatukang
                binding.spesialisDetail.text = response.detailTukang?.spesialis
                binding.pricedetail.text = response.detailTukang?.priceRupiah
                binding.ratingdetail.text = response.detailTukang?.review
                binding.PosisiDetail.text = response.detailTukang?.domisili

                photoUrl = response.detailTukang?.photoUrl
                status = response.detailTukang?.booked == true
                idTukang = response.detailTukang?.tukangId
                Glide.with(this@DetailActivity)
                    .load(response.detailTukang?.photoUrl)
                    .into(binding.photodetial)

                binding.status.text = if(response.detailTukang?.booked == false) "Tersedia" else "Tidak Tersedia"
            }
        }
    }

    companion object{
        const val EXTRA_ID = "extra_id"
    }
}