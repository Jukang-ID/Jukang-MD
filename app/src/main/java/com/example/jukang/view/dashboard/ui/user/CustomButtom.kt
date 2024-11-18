package com.example.jukang.view.dashboard.ui.user

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.jukang.R
import com.example.jukang.data.Room.Alamat
import com.example.jukang.data.Room.AlamatDao
import com.example.jukang.data.Room.AlamatDatabase
import com.example.jukang.data.Room.AlamatLengkap
import com.example.jukang.data.Room.AlamatLengkapDao
import com.example.jukang.data.Room.AlamatLengkapDatabase
import com.example.jukang.databinding.BottomSheetAlamatBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomButtom : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAlamatBinding
    private lateinit var db: AlamatLengkapDatabase
    private lateinit var alamatdao: AlamatLengkapDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetAlamatBinding.inflate(inflater, container, false)

        db = AlamatLengkapDatabase.getDatabase(requireContext())
        alamatdao = db.alamatLengkapDao()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val namauser = requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE)
            .getString("EMAIL", "")

        val kotaJabodetabek = listOf(
            "Jakarta Pusat",
            "Jakarta Utara",
            "Jakarta Barat",
            "Jakarta Selatan",
            "Jakarta Timur",
            "Bogor",
            "Depok",
            "Tangerang",
            "Bekasi"
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, kotaJabodetabek)

        binding.DomisiliInput.setAdapter(adapter)
        binding.DomisiliInput.setOnItemClickListener { adapterView, view, i, l ->
            binding.DomisiliInput.setText(adapterView.getItemAtPosition(i).toString())
        }

        CoroutineScope(Dispatchers.IO).launch {
            val dataalamats = alamatdao.getAlamat(namauser.toString())
            withContext(Dispatchers.Main) {
                if (dataalamats != null) {
                    binding.AlamatInput.setText(dataalamats.alamat)
                    binding.button3.text = "Update alamat"
                    binding.DomisiliInput.setText(dataalamats.kota)
                }
            }
        }


        binding.button3.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val dataalamat = alamatdao.getAlamat(namauser.toString())
                    if (dataalamat != null) {
                           try {
                               alamatdao.update(
                                   dataalamat.copy(alamat = binding.AlamatInput.text.toString(), kota = binding.DomisiliInput.text.toString())
                               )

                           }catch (e: Exception){
                               e.printStackTrace()
                               Toast.makeText(requireContext(), "gagal update", Toast.LENGTH_SHORT).show()
                           }
                    } else {
                        alamatdao.insert(
                            AlamatLengkap(
                                namaUser = namauser.toString(),
                                alamat = binding.AlamatInput.text.toString(),
                                kota = binding.DomisiliInput.text.toString()
                            )
                        )

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }
            dismiss()
        }

        binding.close.setOnClickListener {
            dismiss()
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheet = (dialogInterface as BottomSheetDialog)
                .findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            bottomSheet?.let {
                // Terapkan sudut membulat pada background root Bottom Sheet
                it.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.gambarcorneratas
                )

                // Pastikan behavior Bottom Sheet mengikuti konten
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }
}