package com.example.jukang.view.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.example.jukang.R
import com.example.jukang.databinding.ActivitySplashScreenBinding
import com.example.jukang.view.auth.welcome.WelcomeActivity

class SplashScreen : AppCompatActivity() {

    private lateinit var binding : ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val role = pref.getString("ROLE", null)

        Handler().postDelayed({
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        },2000)
    }
}