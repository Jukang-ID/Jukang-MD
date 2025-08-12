package com.example.jukang.view.dashboard.ui.receipt.order

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jukang.R
import com.example.jukang.databinding.FragmentOrderListBinding
import com.example.jukang.helper.AlertError
import com.example.jukang.helper.adapter.AdapterHistory

class OrderListFragment : Fragment() {

    companion object {
        private const val ARGS_ORDER_STATUS = "order_status"
        fun newInstance(status: String): OrderListFragment {
            return OrderListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGS_ORDER_STATUS, status)
                }
            }
        }
    }

   private lateinit var viewModel : OrderListViewModel
    private var _binding: FragmentOrderListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderListBinding.inflate(inflater, container, false)
        viewModel =ViewModelProvider(this).get(OrderListViewModel::class)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val status = arguments?.getString(ARGS_ORDER_STATUS, "Tidak Ada Status")
        binding.listOrder.layoutManager = LinearLayoutManager(requireContext())


        val pref = requireActivity().getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        val id = pref.getString("UID","")
        viewModel.fetchData(id.toString(), status.toString())

        viewModel.dataTransaksi.observe(viewLifecycleOwner){data->
            val adapter = AdapterHistory(data)
            binding.listOrder.adapter = adapter
        }

        viewModel.loading.observe(viewLifecycleOwner){loading ->
            if(!loading){
                binding.loading.visibility = View.GONE
            }
        }

        viewModel.error.observe(viewLifecycleOwner){error ->
            if(error != null){
                AlertError(
                    requireContext(),
                    "Error",
                    error
                )
            }
        }

        viewModel.isEmpty.observe(viewLifecycleOwner){empty ->
            if(empty){
                binding.placeHolder.visibility = View.VISIBLE
            }
        }

        binding.refresh.setOnRefreshListener {
            viewModel.fetchData(id.toString(), status.toString())
            binding.refresh.isRefreshing = false
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}