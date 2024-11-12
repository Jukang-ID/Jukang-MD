package com.example.jukang.helper.loading

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jukang.R
import com.example.jukang.view.auth.welcome.WelcomeActivity
import com.example.jukang.view.dashboard.MainActivity

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loading)

        Handler().postDelayed({
            checkStatusLogin()
        }, 3000)

    }

    private fun checkStatusLogin() {
        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val token = pref.getString("TOKEN", "")

        if (token == "") {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}

