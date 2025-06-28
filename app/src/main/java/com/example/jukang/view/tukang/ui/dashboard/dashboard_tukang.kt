package com.example.jukang.view.tukang.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.jukang.R
import com.example.jukang.databinding.FragmentDashboardTukangBinding
import com.example.jukang.view.auth.welcome.WelcomeActivity
import com.example.jukang.view.tukang.ui.riwayat.HistoryTukangActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class dashboard_tukang : Fragment() {

    private var _binding: FragmentDashboardTukangBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DashboardViewmodel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardTukangBinding.inflate(inflater,container,false)
        val root: View = binding.root

        val googleSigninOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val pref = requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val name = pref.getString("NAME", null)
        val email = pref.getString("EMAIL", null)
        val photo = pref.getString("PHOTO", null)
        val role = pref.getString("ROLE", null)
        val uid = pref.getString("UID", null)

        binding.refresh.setOnRefreshListener {
            viewModel.fetch(uid.toString())
            binding.refresh.isRefreshing = false
        }

        binding.namaprofile.text = name
        binding.emailprofile.text = email

        Glide.with(requireContext())
            .load(photo)
            .into(binding.imgProfil)

        viewModel = DashboardViewmodel()
        viewModel.fetch(uid.toString())

        viewModel.detailTukang.observe(viewLifecycleOwner, Observer {
            binding.ratingValue.text = it ?: "0"
        })

        viewModel.transaksi.observe(viewLifecycleOwner, Observer {
            binding.valueStat.text = it.toString()
        })


        binding.badge.text = role

        binding.card3.setOnClickListener {
            val intent = Intent(requireContext(), HistoryTukangActivity::class.java)
            startActivity(intent)
        }

//        Toast.makeText(requireContext(), uid, Toast.LENGTH_SHORT).show()

        binding.btnKeluar.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val googlesignclient = GoogleSignIn.getClient(requireContext(), googleSigninOption)
            googlesignclient.signOut().addOnCompleteListener {
                val sharedPreferences =
                    requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()
                val intent = Intent(requireActivity(), WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}