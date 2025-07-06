package com.example.jukang.view.dashboard.ui.home

import RouteRequest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.jukang.R
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.Room.AlamatLengkapDao
import com.example.jukang.data.Room.AlamatLengkapDatabase
import com.example.jukang.data.Room.profileDAO
import com.example.jukang.data.Room.profileDatabase
import com.example.jukang.data.response.Orm
import com.example.jukang.data.response.TukangListItem
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


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inisialisasi binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requireActivity().window.statusBarColor = resources.getColor(R.color.white)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Inisialisasi ViewModel
        homeView = HomeViewModel()

        homeView.dataTukang.observe(viewLifecycleOwner, Observer { list ->
            if (list != null) {
                adapterTukang = AdapterTukang(list as MutableList<TukangListItem>)
                binding.listTukang.adapter = adapterTukang
            }

        })

        homeView.loadingHome.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        homeView.error.observe(viewLifecycleOwner, Observer { error ->
            if (error != null) {
                binding.erromes.text = error
            }
        })

        homeView.isEmpety.observe(viewLifecycleOwner, Observer { empty->
            if (empty){
                binding.caterror.visibility = View.VISIBLE
            }else {
                binding.caterror.visibility = View.GONE
            }

        })

        homeView.notifikasi.observe(viewLifecycleOwner, Observer { notif ->
            if (notif) {
                Toast.makeText(requireContext(), "Tidak ada Koneksi Internet", Toast.LENGTH_SHORT).show()
                binding.caterror.visibility = View.VISIBLE
                binding.erromes.visibility = View.VISIBLE
            }
        })

        // Setup LayoutManager dan Adapter
        binding.listTukang.layoutManager = LinearLayoutManager(requireContext())

        // Menyembunyikan error UI
        binding.erromes.visibility = View.GONE
        binding.caterror.visibility = View.GONE

        dbalamat = AlamatLengkapDatabase.getDatabase(requireContext())
        alamatdao = dbalamat.alamatLengkapDao()

        val sharedPreferences = requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val imageUrl = sharedPreferences.getString(
            "PHOTO",
            "https://static.vecteezy.com/system/resources/previews/002/002/403/non_2x/man-with-beard-avatar-character-isolated-icon-free-vector.jpg"
        )
        val email = sharedPreferences.getString("EMAIL", "null")
        val id = sharedPreferences.getString("UID", "null")

        val (name, _) = getUserData()

        CoroutineScope(Dispatchers.IO).launch{
            val dataalamat = alamatdao.getAlamat(email.toString())
            withContext(Dispatchers.Main){
                if (dataalamat != null){
                    homeView.fetchTukang(dataalamat.kota)
                }else{
                    homeView.fetchTukang("")
                }
            }
        }



        Glide.with(this)
            .load(imageUrl)
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
                        .into(binding.photourl)
                } else {
                    binding.greeting.text = name
                    binding.emailcard.text = email
                    Glide.with(this@HomeFragment)
                        .load(imageUrl)
                        .into(binding.photourl)
                }
            }
        }

        binding.btnRiwayat.setOnClickListener {
            val intent = Intent(requireContext(), HistoryActivity::class.java)
            startActivity(intent)
        }

        binding.swipeContainer.setOnRefreshListener {
            binding.swipeContainer.isRefreshing = false
            CoroutineScope(Dispatchers.IO).launch {
                val data = alamatdao.getAlamat(email.toString())
                withContext(Dispatchers.Main) {
                    if(data != null){
                        homeView.fetchTukang(data.kota)
                    }else{
                        homeView.fetchTukang("")
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
                    val address = getCityname(originLat, originLon)
                    binding.jarak.text = address
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

    fun getCityname (lat:Double,lon:Double):String?{
        val geocoder = Geocoder(requireContext(),Locale.getDefault())
        val address = geocoder.getFromLocation(lat,lon,1)
        return if (address != null && address.size >0){
            address[0].subAdminArea
        }else{
            "Lokasi Tidak Ditemukan"
        }
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
