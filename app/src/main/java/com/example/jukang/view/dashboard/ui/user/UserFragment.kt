package com.example.jukang.view.dashboard.ui.user

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.jukang.R
import com.example.jukang.data.RetrofitClient
import com.example.jukang.data.Room.AlamatLengkapDao
import com.example.jukang.data.Room.AlamatLengkapDatabase
import com.example.jukang.data.Room.profileDAO
import com.example.jukang.data.Room.profileDatabase
import com.example.jukang.databinding.FragmentUserBinding
import com.example.jukang.helper.bottomSheet.ChangeRole
import com.example.jukang.view.auth.welcome.WelcomeActivity
import com.example.jukang.view.form.FormActivity
import com.example.jukang.view.profile.ProfileActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: AlamatLengkapDatabase
    private lateinit var alamatdao: AlamatLengkapDao

    private lateinit var dbProfile: profileDatabase
    private lateinit var profiledao: profileDAO

    private lateinit var viewmodel : userViewmodel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root
        requireActivity().window.statusBarColor = resources.getColor(R.color.primary_button)

        viewmodel = ViewModelProvider(this).get(userViewmodel::class)
        initDb()


        val Email = requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE)
            .getString("EMAIL", "")

        viewmodel.fetchRoleSwittch(Email.toString())

        checkRoleAccept()


        val pref = requireActivity().getSharedPreferences("AUTH",Context.MODE_PRIVATE)
        val id = pref.getString("UID","")

        viewmodel.fetchTukangTransaksi(id.toString())

        viewmodel.dataTukang.observe(viewLifecycleOwner, Observer { size ->
            binding.valueStat.text = size.toString()
        })


        val googleSigninOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        binding.goprofile.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.cardsprofile.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)
        }

        checkStatusProfil(Email.toString())

        binding.refreshLayout.setOnRefreshListener {
            getUserData()
            checkStatusProfil(Email.toString())
            binding.refreshLayout.isRefreshing = false
        }

        getUserData()
        binding.btnLogout2.setOnClickListener {
            val dialogBuild = AlertDialog.Builder(requireActivity())
            dialogBuild.setTitle("Apakah Anda Yakin Ingin Keluar ?")
            dialogBuild.setPositiveButton("Iya Dong") { dialog, which ->

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
            dialogBuild.setNegativeButton("Batal") { dialog, which ->
                dialog.dismiss()
            }
            dialogBuild.show()
        }

        binding.Webview.setOnClickListener{
            val intent = Intent(requireActivity(),FormActivity::class.java)
            startActivity(intent)

        }

        binding.AlamatCard.setOnClickListener {
            val bottomsheet = CustomButtom()
            bottomsheet.updatedListenerUser = {
                checkStatusProfil(Email.toString())
            }
            bottomsheet.show(requireActivity().supportFragmentManager, "BottomSheet")
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.role.setOnClickListener {
            val bottomsheet = ChangeRole.newInstance()
            bottomsheet.show(requireActivity().supportFragmentManager, ChangeRole.TAG)
        }
    }

    fun checkRoleAccept() {
        viewmodel.checkRole.observe(viewLifecycleOwner,{isTukang ->
            if (isTukang){
                binding.role.visibility = View.VISIBLE
            }else{
                binding.role.visibility = View.GONE
            }
        })
    }

    fun checkStatusProfil(namauser: String) {
//        binding.progressBar2.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                try {
//                    binding.progressBar2.visibility = View.GONE
                    alamatdao.getAlamat(namauser).let {
                        binding.alamatSett.text = it.alamat
                    }

                } catch (e: Exception) {
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
                        .into(binding.imgProfil)
                } else {
                    binding.namaprofile.text = nama
                    binding.emailprofile.text = email
                    Glide.with(requireContext())
                        .load(photo)
                        .into(binding.imgProfil)
                }
            }
        }
    }

    private fun initDb() {
        db = AlamatLengkapDatabase.getDatabase(requireContext())
        alamatdao = db.alamatLengkapDao()

        dbProfile = profileDatabase.getDatabase(requireContext())
        profiledao = dbProfile.profiledao()
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