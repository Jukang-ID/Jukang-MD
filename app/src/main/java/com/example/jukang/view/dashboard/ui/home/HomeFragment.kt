package com.example.jukang.view.dashboard.ui.home

import ORSResponse
import RouteRequest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.Room.AlamatLengkapDao
import com.example.jukang.data.Room.AlamatLengkapDatabase
import com.example.jukang.data.Room.profileDAO
import com.example.jukang.data.Room.profileDatabase
import com.example.jukang.data.response.Orm
import com.example.jukang.data.response.TukangItem
import com.example.jukang.databinding.FragmentHomeBinding
import com.example.jukang.helper.adapter.AdapterTukang
import com.example.jukang.view.history.HistoryActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeView: HomeViewModel

    private lateinit var db: profileDatabase
    private lateinit var profiledao: profileDAO

    private lateinit var dbalamat : AlamatLengkapDatabase
    private lateinit var alamatdao : AlamatLengkapDao
    private lateinit var adapterTukang: AdapterTukang
    private val tukangList: MutableList<TukangItem?> = mutableListOf()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inisialisasi binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Inisialisasi ViewModel
        homeView = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Setup LayoutManager dan Adapter
        binding.listTukang.layoutManager = LinearLayoutManager(requireContext())
        adapterTukang = AdapterTukang(emptyList()) // Anda bisa mengganti dengan data awal jika diperlukan
        binding.listTukang.adapter = adapterTukang

        // Menyembunyikan error UI
        binding.erromes.visibility = View.GONE
        binding.caterror.visibility = View.GONE

        dbalamat = AlamatLengkapDatabase.getDatabase(requireContext())
        alamatdao = dbalamat.alamatLengkapDao()

        // Observasi untuk loading
        homeView.loadingHome.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        homeView.dataTukang.observe(viewLifecycleOwner, Observer { list ->
            if (list != null) {
                tukangList.clear()
                tukangList.addAll(list) // Tidak perlu filter, karena tipe cocok
                adapterTukang.updateList(tukangList.filterNotNull()) // Filter null di adapter
            }
        })


        homeView.error.observe(viewLifecycleOwner, Observer { error ->
            if (error != null) {
                binding.erromes.visibility = View.VISIBLE
                binding.caterror.visibility = View.VISIBLE
                binding.erromes.text = error
            }
        })

        homeView.fetchTukang()

        val sharedPreferences = requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val imageUrl = sharedPreferences.getString(
            "PHOTO",
            "https://static.vecteezy.com/system/resources/previews/002/002/403/non_2x/man-with-beard-avatar-character-isolated-icon-free-vector.jpg"
        )
        val email = sharedPreferences.getString("EMAIL", "null")
        val id = sharedPreferences.getString("UID", "null")

        val (name, _) = getUserData()

        Glide.with(this)
            .load(imageUrl)
            .circleCrop()
            .into(binding.photourl)

        db = profileDatabase.getDatabase(requireContext())
        profiledao = db.profiledao()

        binding.progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            val data = profiledao.checkProfile(id.toString())
            withContext(Dispatchers.Main) {
                if (data != null) {
                    binding.greeting.text = data.namaUser
                    binding.emailcard.text = data.email
                    Glide.with(this@HomeFragment)
                        .load(data.profilePhoto)
                        .circleCrop()
                        .into(binding.photourl)
                } else {
                    binding.greeting.text = name
                    binding.emailcard.text = email
                    Glide.with(this@HomeFragment)
                        .load(imageUrl)
                        .circleCrop()
                        .into(binding.photourl)
                }
            }
        }

        binding.btnRiwayat.setOnClickListener {
            val intent = Intent(requireContext(), HistoryActivity::class.java)
            startActivity(intent)
        }

        val number = 0
        val currencyFormat =
            NumberFormat.getCurrencyInstance(Locale("id", "ID")) // Locale Indonesia
        binding.point.text = currencyFormat.format(number)

        binding.profilecard.setOnClickListener {

        }

        binding.pointcard.setOnClickListener {
            val dialogBuild = AlertDialog.Builder(requireActivity())
            dialogBuild.setTitle("Pemberitahuan")
            dialogBuild.setMessage("Fitur ini belum tersedia")
            dialogBuild.setPositiveButton("OK") { dialog, which ->
                dialog.dismiss()
            }
            dialogBuild.show()
        }

        binding.swipeContainer.setOnRefreshListener {
            homeView.fetchTukang()
            binding.swipeContainer.isRefreshing = false
        }

        // Adding SearchView functionality
        setupSearchView()

        Toast.makeText(requireContext(), "Selamat Datang $name", Toast.LENGTH_SHORT).show()

        // Memanggil fungsi getCurrentLocation
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                val data = alamatdao.getAlamat(email.toString())
               withContext(Dispatchers.Main){
                   if (data != null) {
                       if (data.kota == "Jakarta Utara"){
                           getCurrentLocation(-6.121435, 106.774124)
                       } else if (data.kota == "Jakarta Pusat"){
                           getCurrentLocation(-6.175110, 106.865036)
                       } else if (data.kota == "Jakarta Barat"){
                           getCurrentLocation(-6.161184, 106.770914)
                       } else if (data.kota == "Jakarta Selatan"){
                           getCurrentLocation(-6.261493, 106.810600)
                       } else if (data.kota == "Jakarta Timur"){
                           getCurrentLocation(-6.229386, 106.689431)
                       } else if (data.kota == "Bogor"){
                           getCurrentLocation(-6.5952, 106.7896)
                       }else if(data.kota == "Depok") {
                           getCurrentLocation(-6.402484, 106.794240)
                       }else if(data.kota == "Bekasi") {
                           getCurrentLocation(-6.2383, 106.9756)
                       }else if(data.kota == "Tangerang") {
                           getCurrentLocation(-6.1783, 106.6319)
                       }
                   }else{
                       binding.jarak.text = "Silahkan Masukan Alamat"
                       binding.jarak.setOnClickListener {
                           val dialogBuild = AlertDialog.Builder(requireActivity())
                           dialogBuild.setTitle("Pemberitahuan")
                           dialogBuild.setMessage("Silahkan Masukan Alamat Anda")
                           dialogBuild.setPositiveButton("OK") { dialog, which ->
                               dialog.dismiss()
                           }
                           dialogBuild.show()
                       }
                   }
               }
            }
        }

        return root
    }

    fun getCurrentLocation(destlat: Double, destlon: Double) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val currentLat = location.latitude
                    val currentLon = location.longitude

                    getRoutes(currentLat, currentLon, destlat, destlon) // Misal tujuan ke Jakarta
                } else {
                    Toast.makeText(requireContext(), "Tidak dapat mengambil lokasi", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun getRoutes(originLat: Double, originLon: Double, destLat: Double, destLon: Double){
        val request = RouteRequest(
            coordinates = listOf(
                listOf(originLon, originLat),
                listOf(destLon, destLat)
            )
        )
        val call = RetrofitClient.maps.getRoute(request)

        call.enqueue(object : Callback<Orm> {
            override fun onResponse(call: Call<Orm>, response: Response<Orm>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    val routesItem = res?.routes?.get(0)
                    val distance = routesItem?.summary?.distance
                    val duration = routesItem?.summary?.duration

                    val inKm = distance as Double / 1000
                    binding.jarak.text = "User berada ${inKm} KM"
                    binding.jarak.setOnClickListener {
                        val dialogBuild = AlertDialog.Builder(requireActivity())
                        dialogBuild.setTitle("Pemberitahuan")
                        dialogBuild.setMessage("Berada ${distance} meter dari tujuan dan membutuhkan waktu ${duration} detik")
                        dialogBuild.setPositiveButton("OK") { dialog, which ->
                            dialog.dismiss()
                        }
                        dialogBuild.show()
                    }
                }
            }

            override fun onFailure(call: Call<Orm>, t: Throwable) {
                Toast.makeText(requireContext(), "Gagal : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to setup SearchView
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false // Tidak memproses input saat tombol submit ditekan
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter list tukang berdasarkan input pengguna
                val filteredList = tukangList.filter { tukang ->
                    tukang?.let {
                        it.namatukang?.contains(newText ?: "", ignoreCase = true) == true ||
                                it.domisili?.contains(newText ?: "", ignoreCase = true) == true ||
                                it.spesialis?.contains(newText ?: "", ignoreCase = true) == true
                    } == true
                }

                // Periksa apakah hasil pencarian kosong
                if (filteredList.isEmpty()) {
                    // Tampilkan pesan pemberitahuan
                    Toast.makeText(
                        requireContext(),
                        "Tidak ada layanan yang kamu cari",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Perbarui adapter dengan daftar hasil pencarian
                adapterTukang.updateList(filteredList.filterNotNull())
                return true
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getUserData(): Pair<String?, String?> {
        val sharedPreferences = requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("NAME", "tidak ada nama")
        val idToken = sharedPreferences.getString("TOKEN", null)

        return Pair(userName, idToken)
    }
}
