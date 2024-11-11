package com.example.jukang.view.dashboard.ui.artikel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jukang.databinding.FragmentArtikelBinding
import com.example.jukang.helper.AdapterNews

class DashboardFragment : Fragment() {

    private var _binding: FragmentArtikelBinding? = null
    private val binding get() = _binding!!
    private val viewModel : ArtikelViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentArtikelBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.listNew.layoutManager = LinearLayoutManager(requireContext())

        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading){
                binding.loading.visibility = View.VISIBLE
            }else{
                binding.loading.visibility = View.GONE
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            if (error != null){
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.data.observe(viewLifecycleOwner, Observer { list ->
            if (list != null){
                binding.listNew.adapter = AdapterNews(list)
            }
        })

        viewModel.fetchData()
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}