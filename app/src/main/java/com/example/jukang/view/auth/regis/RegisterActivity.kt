package com.example.jukang.view.auth.regis

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jukang.R
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.Register
import com.example.jukang.data.response.registerRequest
import com.example.jukang.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = RegisterViewModel()

        binding.btnBackRegis.setOnClickListener {
            finish()
        }

        binding.btnRegis.setOnClickListener {
            val name = binding.namaInput.text.toString().trim()
            val notelp = binding.notelpon.text.toString().trim()
            val email = binding.emailInputs.text.toString().trim()
            val password = binding.password.text.toString().trim()

            registerForm(name, notelp, email, password)
        }

    }

    private fun registerForm(name: String,notelp: String ,email: String, password: String) {
        val request = registerRequest(name, notelp, email, password)
        val call = RetrofitClient.Jukang.register(request)

        call.enqueue(object : Callback<Register> {
            override fun onResponse(call: Call<Register>, response: Response<Register>) {
                if(response.isSuccessful){
                    val res = response.body()
                    Toast.makeText(this@RegisterActivity, res?.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<Register>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }


}