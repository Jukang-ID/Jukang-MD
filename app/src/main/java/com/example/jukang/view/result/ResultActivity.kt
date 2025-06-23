package com.example.jukang.view.result

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.jukang.databinding.ActivityResultBinding
import com.example.jukang.helper.utils.ImageClassifier
import com.example.jukang.view.dashboard.ui.search.SearchActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    private lateinit var classifier: ImageClassifier

    private fun uriToBitmap(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        classifier = ImageClassifier(this)

        val imageUri = Uri.parse(intent.getStringExtra(UriImage))

        if(imageUri != null){
            setUpImage(imageUri)
            setUpListener(imageUri)
        }else{
            Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
            finish()
        }


    }

    fun setUpListener(imageUri:Uri){
        binding.ulang.setOnClickListener {
            finish()
        }

        binding.scanBTn.setOnClickListener {
            binding.LoadingScan.visibility = android.view.View.VISIBLE

            try {
                // Konversi URI ke Bitmap
                val bitmap = uriToBitmap(imageUri).copy(Bitmap.Config.ARGB_8888, true)
                // Gunakan instance classifier yang sudah ada untuk klasifikasi
                val result = classifier.classify(bitmap)

                // Tampilkan hasil di TextView atau Toast
                Handler().postDelayed({
                    binding.LoadingScan.visibility = android.view.View.GONE
                    val intent = Intent(this@ResultActivity,SearchActivity::class.java).apply {
                        putExtra(SearchActivity.query, result)
                    }
                    startActivity(intent)
                },2000)


            } catch (e: Exception) {

                // Tangani error jika klasifikasi gagal
                Log.d("Result", "setUpListener: ${e.message}", )
                Toast.makeText(this, "Error saat klasifikasi: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    fun setUpImage(imageUri: Uri) {
        binding.preview.setImageURI(imageUri)
    }

    override fun onDestroy() {
        super.onDestroy()
        classifier.close()
    }


    companion object {
        const val UriImage = "uriImage"
    }
}