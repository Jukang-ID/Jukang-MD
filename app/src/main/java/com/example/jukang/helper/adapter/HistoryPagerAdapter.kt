package com.example.jukang.helper.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.jukang.view.dashboard.ui.receipt.order.OrderListFragment

class HistoryPagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {

    // Daftar judul untuk setiap tab
    private val tabTitles = arrayOf("pending", "diterima","Selesai")
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> OrderListFragment.newInstance(tabTitles[position])
            1 -> OrderListFragment.newInstance(tabTitles[position])
            2 -> OrderListFragment.newInstance(tabTitles[position])
            else -> OrderListFragment.newInstance("pending")
        }
    }

    override fun getItemCount(): Int = tabTitles.size


}