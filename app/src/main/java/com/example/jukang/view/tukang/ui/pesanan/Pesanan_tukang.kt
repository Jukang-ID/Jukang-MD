package com.example.jukang.view.tukang.ui.pesanan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jukang.R
import com.example.jukang.databinding.FragmentPesananTukangBinding


class Pesanan_tukang : Fragment() {

    var _binding: FragmentPesananTukangBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPesananTukangBinding.inflate(inflater,container,false)



        // Inflate the layout for this fragment
        return _binding?.root
    }


}