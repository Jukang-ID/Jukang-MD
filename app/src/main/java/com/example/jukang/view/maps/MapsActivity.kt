package com.example.jukang.view.maps

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jukang.R
import com.example.jukang.data.Room.AlamatLengkap
import com.example.jukang.data.Room.AlamatLengkapDao
import com.example.jukang.data.Room.AlamatLengkapDatabase
import com.example.jukang.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import java.io.IOException
import java.util.Locale

class MapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var db: AlamatLengkapDatabase
    private lateinit var dao : AlamatLengkapDao

    private var userMarker: Marker? = null

    private val requestLaucnher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            getCurrentUserLocation()
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun askPermission() {
        when {
            // Memeriksa jika pembiayaan telah diberikan
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
//                maka kenapa
                getCurrentUserLocation()
            }

            else -> {
//                meminta izin langsung pengguna
                requestLaucnher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentUserLocation() {
        // Di sinilah Anda akan menggunakan FusedLocationProviderClient
        // untuk mendapatkan lokasi terakhir atau update lokasi real-time.
        // Contoh:

        // fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? -> ... }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val lat = location.latitude
                val long = location.longitude

                if (lat != null && long != null) {
                    startLocation(lat, long)
                }else{
                    Toast.makeText(this, "Tidak dapat mengambil lokasi", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Tidak dapat mengambil lokasi", Toast.LENGTH_SHORT).show()
            }
        }


        Toast.makeText(this, "Mendapatkan lokasi pengguna...", Toast.LENGTH_SHORT).show()
    }


    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        askPermission()
        setContentView(binding.root)

        db = AlamatLengkapDatabase.getDatabase(this)
        dao = db.alamatLengkapDao()

        mapView = binding.maps
        mapView.setTileSource(TileSourceFactory.MAPNIK)

        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val emailUser = pref.getString("EMAIL","tidak ada email")


        mapView.setMultiTouchControls(true)
        enableMapLongClick()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnAmbilAlamat.setOnClickListener {
            val alamat = binding.tvAddress.text.toString()
            val kota = binding.domisili.text.toString()
            updateAlamat(alamat,kota,emailUser.toString())
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun hideSystemUI(){
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    fun updateAlamat(alamat: String, kota: String,email:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val data = dao.getAlamat(email)
            if (data != null) {
                dao.update(
                    data.copy(
                        alamat = alamat,
                        kota = kota
                    )
                )
            }else{
                dao.insert(
                    AlamatLengkap(
                        alamat = alamat,
                        kota = kota,
                        namaUser = email
                    )
                )
            }
        }
    }

    private fun enableMapLongClick() {
        val receiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                return false // Kita tidak butuh tap biasa
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                if (p != null) {
                    moveToLocation(p)
                    return true
                }
                return false
            }
        }

        val overlayEvents = MapEventsOverlay(receiver)
        mapView.overlays.add(overlayEvents)
    }

    private fun moveToLocation(geoPoint: GeoPoint) {
        // Update posisi marker
        userMarker?.let {
            mapView.overlays.remove(it)
        }
        userMarker = Marker(mapView).apply {
            position = geoPoint
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            title = "Lokasi yang Anda pilih"
        }
        mapView.overlays.add(userMarker)
        mapView.controller.animateTo(geoPoint)
        getAddressFromLocation(geoPoint)
        mapView.invalidate()
    }

    fun startLocation(lat: Double, long: Double) {
        val mapController = mapView.controller
        mapController.setZoom(20.0)
        val startPoint = GeoPoint(lat, long)
        mapController.setCenter(startPoint)

        addMarker(startPoint,"Lokasi Anda")
        getAddressFromLocation(startPoint)
    }

    // --- FUNGSI BARU UNTUK MENGAMBIL ALAMAT ---
    private fun getAddressFromLocation(geoPoint: GeoPoint) {
        // Geocoder hanya boleh dijalankan di background thread
        CoroutineScope(Dispatchers.IO).launch {
            val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
            try {
                // Versi API 33 ke atas memiliki metode asinkron, tapi ini kompatibel untuk versi lama
                val addresses = geocoder.getFromLocation(geoPoint.latitude, geoPoint.longitude, 1)

                if (addresses != null && addresses.isNotEmpty()) {
                    val address = addresses[0]

                    // Ekstrak informasi yang Anda butuhkan
                    val fullAddress = address.getAddressLine(0) ?: "Alamat tidak ditemukan"
                    val kota = address.locality ?: "Kota tidak ditemukan"
                    val provinsi = address.adminArea ?: "Provinsi tidak ditemukan"

                    val lat = address.latitude
                    val lon = address.longitude


                    // Kembali ke Main Thread untuk menampilkan hasil ke UI
                    withContext(Dispatchers.Main) {
                        val addressText = fullAddress
//                        Toast.makeText(this@MapsActivity, addressText, Toast.LENGTH_LONG).show()
                        binding.tvAddress.text = addressText
                        binding.domisili.text = kota
                        binding.tvCoordinates.text = "$lat,$lon"

                    }
                }
            } catch (e: IOException) {
                // Ini terjadi jika ada masalah jaringan atau I/O lainnya
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MapsActivity, "Gagal mendapatkan alamat. Cek koneksi internet.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Fungsi untuk menambahkan marker ke peta
    private fun addMarker(geoPoint: GeoPoint, title: String) {
        val marker = Marker(mapView)
        marker.position = geoPoint

        // Atur posisi anchor agar ikon penanda pas di titik koordinat
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = title // Judul akan muncul saat marker di-tap

        // Tambahkan marker ke dalam lapisan (overlays) peta
        mapView.overlays.add(marker)

        // Refresh peta untuk menampilkan marker yang baru ditambahkan
        mapView.invalidate()
    }

    override fun onRestart() {
        super.onRestart()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}