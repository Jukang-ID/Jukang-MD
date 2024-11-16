package com.example.jukang.view.profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jukang.R
import com.example.jukang.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val name = pref.getString("NAME", "")
        val email = pref.getString("EMAIL", "")
        val phone = pref.getString("PHONE", "-")

        binding.NamaPenguuna.text = name
        binding.profilenama.text = name
        binding.emailfield.text = email
        binding.nofield.text = phone

        binding.backdragon.setOnClickListener {
            finish()
        }

    }
}