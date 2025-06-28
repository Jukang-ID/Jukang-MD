package com.example.jukang.view.tukang.ui.pesanan

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jukang.R
import com.example.jukang.data.response.TransaksiItem
import com.example.jukang.databinding.FragmentPesananTukangBinding
import com.example.jukang.view.tukang.data.AdapterPesanan


class Pesanan_tukang : Fragment() {

    private var _binding: FragmentPesananTukangBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : AdapterPesanan

    private lateinit var viewModel : PesananViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPesananTukangBinding.inflate(inflater,container,false)
        val root: View = binding.root

        val pref = requireActivity().getSharedPreferences("AUTH",Context.MODE_PRIVATE)
        val id = pref.getString("UID","")

//        viewmodel
        viewModel = PesananViewModel()
        viewModel.fetchDataApi(id.toString())


//        setup recycleview
        val Pesanan = binding.listPesananTukang
        Pesanan.layoutManager = LinearLayoutManager(requireContext())

        viewModel.data.observe(viewLifecycleOwner, Observer { list->
            Pesanan.adapter = AdapterPesanan(list as MutableList<TransaksiItem>)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { loading ->

        })

        viewModel.isEmpty.observe(viewLifecycleOwner, Observer { empty ->
            if(empty){
                binding.kosong.visibility = View.VISIBLE
            }else{
                binding.kosong.visibility = View.GONE
            }
        })


        return root
    }


}