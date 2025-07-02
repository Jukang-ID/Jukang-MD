package com.example.jukang.view.form

import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.jukang.R
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.response.PendaftaranReq
import com.example.jukang.data.response.PendaftaranResponse
import com.example.jukang.data.response.UploadResponse
import com.example.jukang.databinding.ActivityFormBinding
import com.example.jukang.helper.utils.createMultipartFromFile
import com.example.jukang.helper.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class FormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormBinding
    private var currentImage: String? = null
    private var currentImage2: String? = null

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            binding.imageViewPhotoKtp.setImageURI(uri)
            binding.kirimBtn.isEnabled = false
            uploadGambarKTP(uri)
        } else {
            currentImage = null
            Toast.makeText(this, "Gagal memilih gambar", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherGallery1 = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            binding.imageViewPhotoProfile.setImageURI(uri)
            binding.kirimBtn.isEnabled = false
            uploadGambarProfile(uri)
        } else {
            Toast.makeText(this, "Gagal memilih gambar", Toast.LENGTH_SHORT).show()
        }
    }

    fun startGallery() {
        launcherGallery1.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun startGallery1() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val name = pref.getString("NAME", "")
        val email = pref.getString("EMAIL", "")
        val id = pref.getString("UID", "")
        val photo = pref.getString("PHOTO", "")

        Glide.with(this)
            .load(photo)
            .into(binding.imageViewPhotoProfile)

        binding.editTextNamaLengkap.setText(name)
        binding.editTextEmail.setText(email)
        binding.editTextUserId.setText(id)


        val cities = listOf(
            "Kabupaten Sambas",
            "Kabupaten Bengkayang",
            "Kabupaten Landak",
            "Kabupaten Mempawah",
            "Kabupaten Sanggau",
            "Kabupaten Sekadau",
            "Kabupaten Sintang",
            "Kabupaten Kapuas Hulu",
            "Kabupaten Ketapang",
            "Kabupaten Kayong Utara",
            "Kabupaten Melawi",
            "Kabupaten Kubu Raya",
            "Kota Pontianak",
            "Kota Singkawang",
            "Kecamatan Pontianak Kota",
            "Kecamatan Pontianak Barat",
            "Kecamatan Pontianak Timur",
            "Kecamatan Pontianak Selatan",
            "Kecamatan Pontianak Utara",
            "Kecamatan Pontianak Tenggara"
        )

        binding.buttonSelectPhotoProfile.setOnClickListener {
            startGallery()
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cities)
        binding.autoCompleteTextViewDomisili.setAdapter(adapter)

        val spesialis = listOf(
            "Reparasi atap",
            "Reparasi saluran air",
            "Reparasi lantai dan dinding",
            "Instalasi listrik",
            "Reparasi aksesoris"
        )

        val adapterSpesialis =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, spesialis)
        binding.autoCompleteTextViewSpesialis.setAdapter(adapterSpesialis)

        binding.buttonSelectPhotoKtp.setOnClickListener {
            startGallery1()
        }

        val photoProfile = if (currentImage2 != null) {
            currentImage2.toString()
        } else {
            photo.toString()
        }

        binding.kirimBtn.setOnClickListener {

            if(currentImage2 == null || currentImage == null){
                Toast.makeText(this, "Gambar tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = PendaftaranReq(
                user_id = id.toString(),
                namalengkap = binding.editTextNamaLengkap.text.toString(),
                email = email.toString(),
                domisili = binding.autoCompleteTextViewDomisili.text.toString(),
                photoprofile = currentImage2.toString(),
                photoktp = currentImage.toString(),
                nomortelp = binding.editTextNomorTelp.text.toString(),
                spesialis = binding.autoCompleteTextViewSpesialis.text.toString(),
            )

            PostPendaftaran(request)

        }
    }

    fun uploadGambarKTP(uri: Uri) {
        val file = uriToFile(this, uri)
        val body = createMultipartFromFile(file)

        val call = RetrofitClient.Jukang.uploadPhoto(body)

        call.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                if (response.isSuccessful) {
                    currentImage = response.body()?.data?.filePath
                    Toast.makeText(
                        this@FormActivity,
                        "Berhasil Menggungah Gambar",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.kirimBtn.isEnabled = true

                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Toast.makeText(this@FormActivity, "Gagal : ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun uploadGambarProfile(uri: Uri) {
        val file = uriToFile(this, uri)
        val body = createMultipartFromFile(file)

        val call = RetrofitClient.Jukang.uploadPhoto(body)

        call.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                if (response.isSuccessful) {
                    currentImage2 = response.body()?.data?.filePath

                    Toast.makeText(
                        this@FormActivity,
                        "Berhasil Menggungah Gambar",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.kirimBtn.isEnabled = true

                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Toast.makeText(this@FormActivity, "Gagal : ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun PostPendaftaran(request: PendaftaranReq) {
        val call = RetrofitClient.Jukang.addPEndaftaran(request)

        call.enqueue(object : Callback<PendaftaranResponse> {
            override fun onResponse(
                call: Call<PendaftaranResponse>,
                response: Response<PendaftaranResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@FormActivity, "Berhasil Mengirimkan", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {
                    Toast.makeText(this@FormActivity, "Gagal", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PendaftaranResponse>, t: Throwable) {

                Toast.makeText(this@FormActivity, "Gagal ${t.localizedMessage}", Toast.LENGTH_SHORT)
                    .show()

            }

        })
    }
}