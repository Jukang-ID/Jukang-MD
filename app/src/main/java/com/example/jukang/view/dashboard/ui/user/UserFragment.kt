package com.example.jukang.view.dashboard.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.jukang.R
import com.example.jukang.data.Room.AlamatDao
import com.example.jukang.data.Room.AlamatDatabase
import com.example.jukang.data.Room.AlamatLengkapDao
import com.example.jukang.data.Room.AlamatLengkapDatabase
import com.example.jukang.data.Room.profileDAO
import com.example.jukang.data.Room.profileDatabase
import com.example.jukang.databinding.FragmentUserBinding
import com.example.jukang.view.auth.login.LoginActivity
import com.example.jukang.view.auth.welcome.WelcomeActivity
import com.example.jukang.view.dashboard.MainActivity
import com.example.jukang.view.profile.ProfileActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: AlamatLengkapDatabase
    private lateinit var alamatdao: AlamatLengkapDao

    private lateinit var dbProfile: profileDatabase
    private lateinit var profiledao: profileDAO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        db = AlamatLengkapDatabase.getDatabase(requireContext())
        alamatdao = db.alamatLengkapDao()

        dbProfile = profileDatabase.getDatabase(requireContext())
        profiledao = dbProfile.profiledao()

        binding.progressBar2.visibility = View.GONE

        binding.goprofile.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.cardsprofile.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)
        }



        val namauser = requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE)
            .getString("EMAIL", "")

        checkStatusProfil(namauser.toString())

        binding.refreshLayout.setOnRefreshListener {
            getUserData()
            checkStatusProfil(namauser.toString())
            binding.refreshLayout.isRefreshing = false
        }

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

        binding.alamat.setOnClickListener {
            val bottomsheet = CustomButtom()
            bottomsheet.show(requireActivity().supportFragmentManager, "BottomSheet")
        }

        return root
    }

    fun checkStatusProfil(namauser: String){
        binding.progressBar2.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                try {
                    binding.progressBar2.visibility = View.GONE
                    alamatdao.getAlamat(namauser).let {
                        binding.alamatPengguna.text = "${it.kota}, ${it.alamat}"
                    }

                }catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getUserData() {
        val pref = requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val nama = pref.getString("NAME", "Tidak Diketahui")
        val email = pref.getString("EMAIL", "Tidak Diketahui")
        val phone = pref.getString("PHONE", "-")
        val id = pref.getString("UID", "123456789987")
        val photo = pref.getString(
            "PHOTO",
            "https://static.vecteezy.com/system/resources/previews/002/002/403/non_2x/man-with-beard-avatar-character-isolated-icon-free-vector.jpg"
        )

        val phoneFormatIdn = phone?.replaceFirst("0", "+62")

        CoroutineScope(Dispatchers.IO).launch {
            val data = profiledao.checkProfile(id.toString())
            withContext(Dispatchers.Main) {
                if (data != null) {
                    binding.namaprofile.text = data.namaUser
                    binding.emailprofile.text = data.email
                    Glide.with(requireContext())
                        .load(data.profilePhoto)
                        .into(binding.profilephoto)
                }else{
                    binding.namaprofile.text = nama
                    binding.emailprofile.text = email
                    Glide.with(requireContext())
                        .load(photo)
                        .into(binding.profilephoto)
                }
            }
        }
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

    companion object {
        const val TAG = "UserFragment"
        const val loadingProfil = false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}