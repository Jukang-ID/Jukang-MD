package com.example.jukang.view.dashboard.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.jukang.R
import com.example.jukang.databinding.FragmentUserBinding
import com.example.jukang.view.auth.login.LoginActivity
import com.example.jukang.view.auth.welcome.WelcomeActivity
import com.example.jukang.view.dashboard.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getUserData()
        binding.logout.setOnClickListener {
            val dialogBuild = AlertDialog.Builder(requireActivity())
            dialogBuild.setTitle("Apakah Anda Yakin Ingin Keluar ?")
            dialogBuild.setPositiveButton("Iya Dong") { dialog, which ->
                logout()
            }
            dialogBuild.setNegativeButton("Batal") { dialog, which ->
                dialog.dismiss()
            }
            dialogBuild.show()
        }

        return root
    }

    private fun getUserData() {
        val pref = requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val nama = pref.getString("NAME", "Tidak Diketahui")
        val email = pref.getString("EMAIL", "Tidak Diketahui")
        val id = pref.getString("UID", "123456789987")
        val photo = pref.getString(
            "PHOTO",
            "https://static.vecteezy.com/system/resources/previews/002/002/403/non_2x/man-with-beard-avatar-character-isolated-icon-free-vector.jpg"
        )

        binding.namaprofile.text = nama
        binding.emailprofile.text = email
        binding.iduser.text = "Id : $id"
        Glide.with(requireContext())
            .load(photo)
            .into(binding.profilephoto)
    }

    private fun logout() {
        val sharedPreferences = requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(requireActivity(), WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}