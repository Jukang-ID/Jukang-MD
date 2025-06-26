package com.example.jukang.view.auth.welcome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.jukang.R
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.Register
import com.example.jukang.data.response.registerRequest
import com.example.jukang.databinding.ActivityWelcomeBinding
import com.example.jukang.helper.loading.LoadingActivity
import com.example.jukang.view.auth.login.LoginActivity
import com.example.jukang.view.auth.regis.RegisterActivity
import com.example.jukang.view.dashboard.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkStatusLogin()
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.btnLoginGoogle.setOnClickListener {
            signInwithGoogle()
        }


    }

    fun fetchUser(name: String, notelp: String, email: String, password: String, photo: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.Jukang.getUserByEmail(email)

            if (response.listUser?.isEmpty() == true) {
                registerForm(name, notelp, email, password, photo)
            } else {

                val role = response.listUser?.get(0)?.role
                val user_id = response.listUser?.get(0)?.userId

                val sharedPref = getSharedPreferences("AUTH", MODE_PRIVATE)
                val edit = sharedPref.edit()
                edit.putString("ROLE", role)
                edit.putString("UID", user_id)
                edit.apply()

//                Toast.makeText(this@WelcomeActivity, role, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@WelcomeActivity, LoadingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private fun checkStatusLogin() {
        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val token = pref.getString("TOKEN", null)
        val id = pref.getString("UID", null)
        val role = pref.getString("ROLE", null)


        when (role) {
            "tukang" -> {
                val intent =
                    Intent(this, com.example.jukang.view.tukang.ui.MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }

            "pengguna" -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()

            }
            else -> null

        }
    }


    private fun registerForm(
        name: String,
        notelp: String,
        email: String,
        password: String,
        photo: String
    ) {
        val request = registerRequest(name, notelp, email, password, photo)
        val call = RetrofitClient.Jukang.register(request)

        call.enqueue(object : Callback<Register> {
            override fun onResponse(call: Call<Register>, response: Response<Register>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    val role = res?.data?.user?.role
                    val uid = res?.data?.user?.userId

                    val sharedPref = getSharedPreferences("AUTH", MODE_PRIVATE)
                    val edit = sharedPref.edit()
                    edit.putString("ROLE", role)
                    edit.putString("UID", uid)
                    edit.apply()

                    Toast.makeText(this@WelcomeActivity, "Registrasi Berhasil", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@WelcomeActivity, LoadingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Register>, t: Throwable) {
                Toast.makeText(this@WelcomeActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }


    private fun signInwithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign-in failed", e)
                Toast.makeText(this, "Failed to sign in with Google", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val token = account?.idToken.toString()
                    val email = account?.email.toString()
                    val name = account?.displayName.toString()
                    val photo = account?.photoUrl.toString()
                    val uid = account?.id.toString()

                    fetchUser(name, "", email, uid, photo)
                    saveData(token, email, name, photo)

                    Log.d(TAG, "signInWithCredential:success")

                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }


    private fun saveData(token: String, email: String, name: String, photo: String) {
        val sharedPref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val edit = sharedPref.edit()
        edit.putString("TOKEN", token)
        edit.putString("EMAIL", email)
        edit.putString("NAME", name)
        edit.putString("PHOTO", photo)

        edit.apply()

    }


    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "LoginActivity"
    }


}