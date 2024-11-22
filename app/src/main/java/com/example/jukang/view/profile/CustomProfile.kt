package com.example.jukang.view.profile

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.jukang.R
import com.example.jukang.data.Room.profileDAO
import com.example.jukang.data.Room.profileDatabase
import com.example.jukang.data.Room.profileLengkap
import com.example.jukang.databinding.BottomSheetProfileBinding
import com.example.jukang.helper.OnProfileUpdatedListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class CustomProfile : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetProfileBinding
    private var currentImageuri: Uri? = null

    private lateinit var db: profileDatabase
    private lateinit var profiledao: profileDAO

//    val profileUpdateListener : OnProfileUpdatedListener? = null

    var profileUpdatedListener: (() -> Unit)? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetProfileBinding.inflate(inflater, container, false)
        db = profileDatabase.getDatabase(requireContext())
        profiledao = db.profiledao()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val id = pref.getString("UID", "")
        val nameuser = pref.getString("NAME", "")
        val emailuser = pref.getString("EMAIL", "")
        val phoneuser = pref.getString("PHONE", "")
        val photo = pref.getString(
            "PHOTO",
            "https://static.vecteezy.com/system/resources/previews/002/002/403/non_2x/man-with-beard-avatar-character-isolated-icon-free-vector.jpg"
        ) ?: ""

        CoroutineScope(Dispatchers.IO).launch {
            val data = profiledao.checkProfile(id.toString())
            withContext(Dispatchers.Main) {
                if (data != null) {
                    binding.NohpInput.setText(data.nomorTelp)
                    binding.Namainput.setText(data.namaUser ?: nameuser)
                    binding.emailInputs.setText(emailuser)
                    binding.photoUri.setText(data.profilePhoto)
                    binding.btnDragon.text = "Update Profile"
                } else {
                    binding.NohpInput.setText("-")
                    binding.Namainput.setText(nameuser)
                    binding.emailInputs.setText(emailuser)
                    binding.photoUri.setText(photo)
                    binding.btnDragon.text = "Update Profile"
                }
            }
        }

        binding.uploudButton.setOnClickListener {
            gallery()

        }

        binding.btnDragon.setOnClickListener {
            val name = binding.Namainput.text.toString()
            val email = binding.emailInputs.text.toString()
            val phone = binding.NohpInput.text.toString()
            val photo = binding.photoUri.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val data = profiledao.checkProfile(id.toString())
                val request = profileLengkap(
                    id = 0,
                    id_user = id.toString(),
                    namaUser = name,
                    nomorTelp = phone,
                    email = email,
                    profilePhoto = photo
                )

                withContext(Dispatchers.Main) {
                    if (data != null) {
                        profiledao.updatedata(
                            data.copy(
                                namaUser = name,
                                nomorTelp = phone,
                                email = email,
                                profilePhoto = photo
                            )
                        )
                        showToast("Profile berhasil diupdate")
                    } else {
                        profiledao.insertdata(request)
                        showToast("Profile berhasil dibuat")
                    }
                    profileUpdatedListener?.invoke()
                    dismiss()
                }
            }

        }

    }



    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    private fun gallery() {
        launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun saveImageToInternalStorage(uri: Uri): String {
        val contentResolver = requireContext().contentResolver
        val fileName = "profile_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, fileName)

        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: Exception) {
            showToast("Gagal menyimpan gambar: ${e.message}")
        }

        return file.absolutePath
    }


    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageuri = uri
            val saveImagePath = saveImageToInternalStorage(uri)
            binding.photoUri.setText(saveImagePath)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheet = (dialogInterface as BottomSheetDialog)
                .findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            bottomSheet?.let {
                it.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.gambarcorneratas
                )

                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    companion object {
        const val TAG = "CustomProfile"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}