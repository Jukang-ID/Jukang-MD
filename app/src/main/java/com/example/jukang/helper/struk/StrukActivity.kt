package com.example.jukang.helper.struk

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jukang.R
import com.example.jukang.databinding.ActivityStrukBinding
import com.example.jukang.view.dashboard.MainActivity

class StrukActivity : AppCompatActivity() {

    private lateinit var binding:ActivityStrukBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStrukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selesaihistory.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        val nama = intent.getStringExtra(namatukangs)
        val idtukang = intent.getStringExtra(id)
        val tanggal = intent.getStringExtra(tanggal)
        val method = intent.getStringExtra(method)
        val total = intent.getStringExtra(total)

        binding.nama.text = nama
        binding.date.text = tanggal
        binding.totalStruk.text = total
        binding.method.text = method
        binding.idTukanng.text = idtukang

    }
    companion object{
        const val namatukangs = "namatukang"
        const val id= "id"
        const val tanggal = "tanggal"
        const val method = "method"
        const val total = "total"

    }
}