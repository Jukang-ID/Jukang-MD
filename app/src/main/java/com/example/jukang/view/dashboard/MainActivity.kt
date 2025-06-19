package com.example.jukang.view.dashboard

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.jukang.R
import com.example.jukang.databinding.ActivityMainBinding
import com.example.jukang.view.auth.login.LoginActivity
import com.example.jukang.view.auth.welcome.WelcomeActivity
import com.example.jukang.view.dashboard.ui.artikel.DashboardFragment
import com.example.jukang.view.dashboard.ui.home.HomeFragment
import com.example.jukang.view.dashboard.ui.user.UserFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkStatusLogin()

        binding.fab.setOnClickListener {
            Toast.makeText(this, "Purple Plus Button Clicked!", Toast.LENGTH_SHORT).show()
            // Here you can add logic for when the FAB is clicked,
            // such as opening a dialog, starting a new activity, or displaying something.
        }


        supportActionBar?.hide()
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

    }
    private fun checkStatusLogin(){
        val sharedPreferences = getSharedPreferences("AUTH", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)
        val id = sharedPreferences.getString("UID",null)
        if (id == null){
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}