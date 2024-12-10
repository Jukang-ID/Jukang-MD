package com.example.jukang.view.profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.jukang.R
import com.example.jukang.data.Room.profileDAO
import com.example.jukang.data.Room.profileDatabase
import com.example.jukang.databinding.ActivityProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var db : profileDatabase
    private lateinit var profiledao: profileDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = profileDatabase.getDatabase(this)
        profiledao = db.profiledao()


        dataUser()

        binding.main.setOnRefreshListener {
            dataUser()
            binding.main.isRefreshing = false
        }

        binding.backdragon.setOnClickListener {
            finish()
        }

        binding.Edit.setOnClickListener {
            val custom = CustomProfile()
            custom.profileUpdatedListener ={
                dataUser()
            }
            custom.show(supportFragmentManager, "CustomProfile")

        }

    }

    fun dataUser(){
        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val id = pref.getString("UID", "")
        val name = pref.getString("NAME", "")
        val email = pref.getString("EMAIL", "")
        val phone = pref.getString("PHONE", "-")

        val photoProfile = pref.getString(
            "PHOTO",
            "https://static.vecteezy.com/system/resources/previews/002/002/403/non_2x/man-with-beard-avatar-character-isolated-icon-free-vector.jpg"
        ) ?: ""
        CoroutineScope(Dispatchers.IO).launch {
            val data = profiledao.checkProfile(id.toString())
            withContext(Dispatchers.Main) {
                if(data!=null){
                    binding.NamaPenguuna.text = data.namaUser
                    binding.profilenama.text = data.namaUser
                    binding.emailfield.text = data.email
                    binding.nofield.text = data.nomorTelp
                    Glide.with(this@ProfileActivity)
                        .load(data.profilePhoto)
                        .into(binding.imageView15)
                }else{
                    binding.NamaPenguuna.text = name
                    binding.profilenama.text = name
                    binding.emailfield.text = email
                    binding.nofield.text = phone
                    Glide.with(this@ProfileActivity)
                        .load(photoProfile)
                        .into(binding.imageView15)
                }
            }
        }
    }
}