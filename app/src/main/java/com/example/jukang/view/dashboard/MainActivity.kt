package com.example.jukang.view.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
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
import com.example.jukang.view.camerax.CameraActivity
import com.example.jukang.view.chatbot.ChatActivity
import com.example.jukang.view.dashboard.ui.artikel.DashboardFragment
import com.example.jukang.view.dashboard.ui.home.HomeFragment
import com.example.jukang.view.dashboard.ui.user.UserFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("Home", "onCreate: permission granted")
        } else {
            Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()
        }

    }

    fun PermissionLocation() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
                -> {
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "PermissionLocation: IsGranted")

            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    fun permissionNotification() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG, "permissionNotification: Permission granted")
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    fun PermissionCamera() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG, "PermissionCamera: IsGranted")
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.WHITE // Sesuai permintaan, bagian bawah jadi putih.

        // 3. Ubah ikon di status bar & navigation bar menjadi gelap.
        // Ini penting! Karena background-nya sekarang terang (putih), ikonnya harus gelap biar kelihatan.
        WindowCompat.getInsetsController(window, window.decorView).let { controller ->
            controller.isAppearanceLightStatusBars = true
            controller.isAppearanceLightNavigationBars = true
        }
        PermissionCamera()
        PermissionLocation()

        permissionNotification()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkStatusLogin()

        binding.fab.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up, R.anim.fade_out)

        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val sharedPreferences = getSharedPreferences("AUTH", MODE_PRIVATE)
        val role = sharedPreferences.getString("ROLE", null)

//        Toast.makeText(this, role, Toast.LENGTH_SHORT).show()

        binding.ChatAi.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

    }

    private fun checkStatusLogin() {
        val sharedPreferences = getSharedPreferences("AUTH", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)
        val id = sharedPreferences.getString("UID", null)
        if (id == null) {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    companion object {
        private const val TAG = "Home"
    }
}