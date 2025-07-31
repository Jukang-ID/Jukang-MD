package com.example.jukang.view.payment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.jukang.R
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.Room.AlamatLengkapDao
import com.example.jukang.data.Room.AlamatLengkapDatabase
import com.example.jukang.data.response.OrderMidtransResponse
import com.example.jukang.data.response.Payment
import com.example.jukang.data.response.PaymentOrderRequest
import com.example.jukang.data.response.Tukang
import com.example.jukang.data.response.TukangReq
import com.example.jukang.data.response.paymentReq
import com.example.jukang.databinding.ActivityPaymentBinding
import com.example.jukang.helper.struk.StrukActivity
import com.example.jukang.service.createNotificationChannel
import com.example.jukang.service.showNotification
import com.example.jukang.view.dashboard.MainActivity
import com.example.jukang.view.maps.MainApplication
import com.example.jukang.view.maps.MapsActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var db: AlamatLengkapDatabase
    private lateinit var alamatdao: AlamatLengkapDao


    private val CHANNEL_ID = "Pembayaran"
    private val NOTIFICATION_ID = 101

    var lat: String? = null
    var lon: String? = null
    var DomisiliPengguna: String? = null

    var deskripsi: String? = null
    var tanggal: String? = null
    var alamat: String? = null
    var nomorTelepon: String? = null

    var idTukangs: String? = null
    var id_user: String? = null
    var photoProfile: String? = null

    var statusBooked: Boolean? = null
    var ratingTukang: String? = null




    private var currentImage: Uri? = null

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult =
                        it.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                    if(transactionResult?.status == UiKitConstants.STATUS_SUCCESS){
                        val request = paymentReq(
                            user_id = id_user.toString(),
                            tukang_id = idTukangs.toString(),
                            deskripsi = deskripsi.toString(),
                            tanggal = tanggal.toString(),
                            alamat = alamat.toString(),
                            metodePembayaran = transactionResult.paymentType,
                            domisili = DomisiliPengguna.toString(),
                            lat = lat.toString(),
                            long = lon.toString(),
                            nomor_telpon = nomorTelepon,
                            photoprofile = photoProfile
                        )
                        statusBooked?.let { status ->
                            paymentMethod(request,
                                status,ratingTukang.toString())
                        }
                    }else{
                        handleTransactionResult(transactionResult)
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        setContentView(binding.root)
        binding.progressBar2.visibility = View.GONE

        createNotificationChannel(
            CHANNEL_ID, this
        )

        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val id = pref.getString("UID", "").toString()
        val nama = pref.getString("NAME", "").toString()
        val photo = pref.getString("PHOTO", "").toString()

        val namatukang = intent.getStringExtra(namaTukang).toString()
        val harga = intent.getStringExtra(harga).toString()
        val spesialis = intent.getStringExtra(spesialis).toString()
        val gambar = intent.getStringExtra(gambarTukang).toString()
        val status = intent.getBooleanExtra(booked, false)
        val rating = intent.getStringExtra(rating).toString()
        val idTukang = intent.getStringExtra(idTukang).toString()
        val email = pref.getString("EMAIL", "").toString()
        val domisili = intent.getStringExtra(domisili).toString()
        val amount = intent.getIntExtra(PaymentActivity.amount, 0)


        db = AlamatLengkapDatabase.getDatabase(this)
        alamatdao = db.alamatLengkapDao()

        CoroutineScope(Dispatchers.IO).launch {
            val alamat = alamatdao.getAlamatLive(email)
            withContext(Dispatchers.Main) {
                alamat.observe(this@PaymentActivity) { alamat ->
                    if (alamat != null) {
                        binding.Alamat.setText(alamat.alamat)
                        lat = alamat.lat
                        lon = alamat.lon
                        DomisiliPengguna = alamat.kota
                    } else {
                        binding.Alamat.setText("")
                        lat = ""
                        lon = ""
                        DomisiliPengguna = ""
                    }
                }
            }
        }



        Glide.with(this@PaymentActivity)
            .load(gambar)
            .into(binding.pototukang)

        binding.appbar.setNavigationOnClickListener {
            finish()
        }

        binding.tukangNm.text = namatukang
        binding.harga.text = harga
        binding.totalHarga.text = harga

        binding.btnBAckPay.setOnClickListener {
            finish()
        }

        binding.tanggal.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnUploadPhoto.setOnClickListener {
            launchGallery()
        }

//        Toast.makeText(this, "$amount", Toast.LENGTH_SHORT).show()

        binding.btnPayment.setOnClickListener {
            binding.progressBar2.visibility = View.VISIBLE
            deskripsi = binding.deskripsiPerbaikan.text.toString().trim()
            tanggal = binding.tanggal.text.toString().trim()
            alamat = binding.Alamat.text.toString().trim()
            nomorTelepon = binding.nomorTelpon.text.toString().trim()
            idTukangs = idTukang
            id_user = id
            photoProfile = photo
            ratingTukang = rating
            statusBooked = status

//            Toast.makeText(this, "$idTukang $id_user", Toast.LENGTH_SHORT).show()


//            val intent = Intent(this@PaymentActivity, StrukActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
////            intent.putExtra(StrukActivity.id_transaksi,data?.idTransaksi)
//            startActivity(intent)

            val requestToken = PaymentOrderRequest(
                total = amount,
                user_id = id
                )
            fetchTransactionToken(requestToken)
        }



        binding.btnLocationMarked.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)

        }
    }

    private fun fetchTransactionToken(request : PaymentOrderRequest) {
        val call = RetrofitClient.Jukang.getTokenMidtrans(request)

        call.enqueue(object : Callback<OrderMidtransResponse>{
            override fun onResponse(
                call: Call<OrderMidtransResponse>,
                response: Response<OrderMidtransResponse>
            ) {
                if(response.isSuccessful){
                    val data = response.body()
                    val snaptoken = data?.token

                    startMidtransPayment(snaptoken.toString())

                }
            }

            override fun onFailure(p0: Call<OrderMidtransResponse>, p1: Throwable) {
                Toast.makeText(this@PaymentActivity, "Failed : ${p1.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun startMidtransPayment(token: String) {
        Log.d("PaymentFlowDebug", "3. Calling startMidtransPayment() with token: $token")

        MainApplication.uiKitApi?.startPaymentUiFlow(
            this,
            launcher,
            token
        )
    }

    private fun handleTransactionResult(result: TransactionResult?) {
        when (result?.status) {
            UiKitConstants.STATUS_SUCCESS -> {
                Toast.makeText(this, "Transaction Success", Toast.LENGTH_LONG).show()
            }

            UiKitConstants.STATUS_PENDING -> {
                Toast.makeText(this, "Transaction Pending", Toast.LENGTH_LONG).show()
            }

            UiKitConstants.STATUS_FAILED -> {
                Toast.makeText(this, "Transaction Failed", Toast.LENGTH_LONG).show()
            }

            else -> {
                Toast.makeText(this, "Transaction Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                binding.tanggal.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun paymentMethod(
        payment: paymentReq,
        status: Boolean,
        rating: String
    ) {
        val call = RetrofitClient.Jukang.addtransaksi(payment)

        call.enqueue(object : Callback<Payment> {
            override fun onResponse(call: Call<Payment>, response: Response<Payment>) {
                if (response.isSuccessful) {
                    binding.progressBar2.visibility = View.GONE
                    val data = response.body()?.data?.transaksi
                    update(data?.dataTukang?.namatukang.toString(), data?.dataTukang?.spesialis.toString(), rating, status, payment.tukang_id)
                    showNotification(
                        this@PaymentActivity,
                        CHANNEL_ID,
                        NOTIFICATION_ID,
                        "Pembayaran Dilakukan",
                        "saat ini pembayaran sedang dilakukan"
                    )
                    Toast.makeText(this@PaymentActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@PaymentActivity, StrukActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra(StrukActivity.id_transaksi,data?.idTransaksi)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@PaymentActivity,
                        "Gagal : ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar2.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<Payment>, t: Throwable) {
                Toast.makeText(
                    this@PaymentActivity,
                    "Barang sudah di booking orang",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("TAG", "onFailure: ${t.localizedMessage}")
            }
        })
    }

    private fun update(
        namatukang: String,
        spesialis: String,
        review: String,
        booked: Boolean,
        id: String
    ) {
        val requestUpdate = TukangReq(
            namatukang,
            spesialis,
            review,
            booked = true
        )
        val call = RetrofitClient.Jukang.updateTukang(requestUpdate, id)
        call.enqueue(object : Callback<Tukang> {
            override fun onResponse(call: Call<Tukang>, response: Response<Tukang>) {
                if (response.isSuccessful) {
                    Log.d(Tag, "onResponse: ${response.body()}")
                } else {
                    Log.d(Tag, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Tukang>, t: Throwable) {

            }
        })

    }

    fun launchGallery() {
        launcherGallary.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallary = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImage = uri
            binding.imageViewSelectedPhoto.setImageURI(currentImage!!)
        } else {
            Toast.makeText(this, "Gagal memilih gambar", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkStatus() {
        if (binding.tukangNm.text.toString().isEmpty()) {
            binding.tukangNm.error = "Nama Tukang tidak boleh kosong"
            finish()
        }
    }


    companion object {
        const val namaTukang = "namaTukang"
        const val harga = "harga"
        const val spesialis = "spesialis"
        const val gambarTukang = "https://i.pravatar.cc/300"
        const val Tag = "PaymentActivity"
        const val rating = "rating"
        const val booked = "status"
        const val idTukang = "id"
        const val domisili = "domisili"
        const val amount = ""
    }
}