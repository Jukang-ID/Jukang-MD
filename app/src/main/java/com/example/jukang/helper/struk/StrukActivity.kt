package com.example.jukang.helper.struk

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jukang.R
import com.example.jukang.databinding.ActivityStrukBinding
import com.example.jukang.view.dashboard.MainActivity
import pl.droidsonroids.gif.GifImageView

class StrukActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStrukBinding

    private lateinit var viewModel: StrukViewModel

    private var isShow:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStrukBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id_transaksi = intent.getStringExtra(id_transaksi).toString()

        binding.check.visibility = View.GONE
        binding.card.visibility = View.VISIBLE

        Handler().postDelayed({
            binding.check.visibility = View.VISIBLE
            binding.card.visibility = View.GONE
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                // Kasih transisi biar smooth
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            },2000)
        },2000)



        binding.selesaihistory.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }


    }

    companion object {
        const val id_transaksi = "id_transaksi"


    }
}