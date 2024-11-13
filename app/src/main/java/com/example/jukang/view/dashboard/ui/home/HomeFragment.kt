package com.example.jukang.view.dashboard.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.jukang.databinding.FragmentHomeBinding
import com.example.jukang.helper.adapter.AdapterTukang
import com.example.jukang.view.history.HistoryActivity
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeView: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.listTukang.layoutManager = LinearLayoutManager(requireContext())

        homeView = HomeViewModel()

        homeView.loadingHome.observe(viewLifecycleOwner, Observer { loading->
            if(loading){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        })

        homeView.dataTukang.observe(viewLifecycleOwner, Observer { list ->
            if (list != null){
                val adapter = AdapterTukang(list)
                binding.listTukang.adapter = adapter
            }
        })

        homeView.error.observe(viewLifecycleOwner, Observer { error ->
            if (error != null){
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        })

        homeView.fetchTukang()

        val sharedPreferences = requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val imageUrl = sharedPreferences.getString(
            "PHOTO",
            "https://static.vecteezy.com/system/resources/previews/002/002/403/non_2x/man-with-beard-avatar-character-isolated-icon-free-vector.jpg"
        )
        val email = sharedPreferences.getString("EMAIL", "null")

        val (name, _) = getUserData()

        Glide.with(this)
            .load(imageUrl)
            .circleCrop()
            .into(binding.photourl)

        binding.progressBar.visibility = View.VISIBLE

        binding.greeting.text = name
        binding.emailcard.text = email

        binding.btnRiwayat.setOnClickListener {
            val intent = Intent(requireContext(), HistoryActivity::class.java)
            startActivity(intent)
        }

        val number = 0
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID")) // Locale Indonesia
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



        return root
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