package com.example.jukang.view.auth.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.Login
import com.example.jukang.data.response.loginRequest
import com.example.jukang.databinding.ActivityLoginBinding
import com.example.jukang.helper.loading.LoadingActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var viewModel : loginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        viewModel = loginViewModel()



        binding.btnBackLogin.setOnClickListener {
            finish()
        }

        binding.btnRegis.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            login(email, password)
        }

    }

    private fun login(email:String, password:String){
        val req = loginRequest(email, password)
        val call = RetrofitClient.Jukang.login(req)

        call.enqueue(object : Callback<Login> {
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                if(response.isSuccessful){
                    val res = response.body()
                    Toast.makeText(this@LoginActivity, res?.message, Toast.LENGTH_SHORT).show()
                    if(res != null){
                        val id = res.data?.user?.userId
                        val name = res.data?.user?.namalengkap
                        val email = res.data?.user?.email
                        val phone = res.data?.user?.nomortelp

                        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
                        val editor = pref.edit()
                        editor.putString("UID", id)
                        editor.putString("NAME", name)
                        editor.putString("EMAIL", email)
                        editor.putString("PHONE", phone)
                        editor.apply()

                        val intent = Intent(this@LoginActivity, LoadingActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<Login>, t: Throwable) {

            }
        })
    }

}