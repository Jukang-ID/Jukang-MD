package com.example.jukang.view.dashboard.ui.receipt

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.jukang.R
import com.example.jukang.databinding.FragmentReceiptBinding
import com.example.jukang.helper.adapter.HistoryPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ReceiptFragment : Fragment() {


//    private val viewModel: ReceiptViewModel = ViewModelProvider(this).get(ReceiptViewModel::class)

    private var _binding: FragmentReceiptBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HistoryPagerAdapter(requireActivity())
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tabLayout,binding.viewpager){tab, position ->
            tab.text = when(position){
                0 -> "pending"
                1 -> "diterima"
                2 -> "Selesai"
                else -> "pending"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}