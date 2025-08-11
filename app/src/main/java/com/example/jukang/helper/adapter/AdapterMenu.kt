package com.example.jukang.helper.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.jukang.databinding.CardMenuBinding
import com.example.jukang.helper.model.menu_layanan

class AdapterMenu(private val listMenu : List<menu_layanan>): RecyclerView.Adapter<AdapterMenu.MenuViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuViewHolder {
        val binding = CardMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MenuViewHolder,
        position: Int
    ) {
        holder.bind(listMenu[position])
    }

    override fun getItemCount(): Int = listMenu.size

    class MenuViewHolder(private val binding: CardMenuBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(menu : menu_layanan){
            binding.titleMenu.text = menu.name
            menu.icon?.let {
                binding.floatingActionButton.setImageResource(it)
            }
            menu.color?.let {colorResId ->
                val context = binding.root.context
                val colorStateList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, colorResId)
                )
                binding.floatingActionButton.backgroundTintList = colorStateList

            }
        }
    }


}