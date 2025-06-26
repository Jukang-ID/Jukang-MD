package com.example.jukang.helper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.jukang.databinding.ItemChatBotBinding
import com.example.jukang.databinding.TextlayoutBinding
import com.example.jukang.helper.Message

class AdapterChat(private val messages: List<Message>) : RecyclerView.Adapter<AdapterChat.MessageViewHolder>() {

    // Definisikan konstanta untuk tipe view
    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_BOT = 2
    }

    /**
     * ViewHolder umum yang bisa menampung binding apa pun.
     * Kita akan melakukan 'cast' ke binding yang spesifik nanti.
     */
    class MessageViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Fungsi ini menentukan layout mana yang akan digunakan berdasarkan posisi.
     */
    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isFromUser) {
            VIEW_TYPE_USER
        } else {
            VIEW_TYPE_BOT
        }
    }

    /**
     * Di sini kita inflate layout yang benar (user atau bot) berdasarkan viewType.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = if (viewType == VIEW_TYPE_USER) {
            TextlayoutBinding.inflate(inflater, parent, false)
        } else {
            ItemChatBotBinding.inflate(inflater, parent, false)
        }
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    /**
     * Di sini kita mengisi data ke dalam view yang benar.
     */
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        when (holder.itemViewType) {
            VIEW_TYPE_USER -> {
                // Cast binding ke tipe yang benar dan set teksnya
                val userBinding = holder.binding as TextlayoutBinding
                userBinding.messageTextView.text = message.text
            }
            VIEW_TYPE_BOT -> {
                // Cast binding ke tipe yang benar dan set teksnya
                val botBinding = holder.binding as ItemChatBotBinding
                botBinding.messageTextView.text = message.text
            }
        }
    }
}