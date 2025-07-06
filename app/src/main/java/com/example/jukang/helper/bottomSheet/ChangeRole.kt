package com.example.jukang.helper.bottomSheet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jukang.R
import com.example.jukang.databinding.ChangeRoleBsBinding
import com.example.jukang.view.dashboard.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChangeRole : BottomSheetDialogFragment() {

    // Companion object untuk membuat instance baru dengan cara yang bersih
    companion object {
        const val TAG = "CustomBottomSheet"

        fun newInstance(): ChangeRole {
            return ChangeRole()
        }
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    private lateinit var binding: ChangeRoleBsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChangeRoleBsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val role = requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE)
            .getString("ROLE", "")
        val edit = requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE).edit()

        binding.tukang.setOnClickListener {
            if(role == "tukang"){
                dismiss()
            }else{
                edit.putString("ROLE", "tukang")
                edit.apply()
                val intent = Intent(requireActivity(),com.example.jukang.view.tukang.ui.MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                dismiss()
            }
        }

        binding.costumer.setOnClickListener {
            if(role == "pengguna"){
                dismiss()
            }else{
                edit.putString("ROLE", "pengguna")
                edit.apply()
                val intent = Intent(requireActivity(),MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                dismiss()
            }
        }


    }

}