package com.example.jukang.view.chatbot

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jukang.BuildConfig
import com.example.jukang.R
import com.example.jukang.databinding.ActivityChatBinding
import com.example.jukang.helper.Message
import com.example.jukang.helper.adapter.AdapterChat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var generativeModel: GenerativeModel
    private val messageList = mutableListOf<com.example.jukang.helper.Message>()
    private lateinit var messageAdapter: AdapterChat
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi UI
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageEditText = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.sendButton)

        // Setup RecyclerView
        messageAdapter = AdapterChat(messageList)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        // Inisialisasi Model Gemini
        generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash-latest",
            apiKey =  BuildConfig.GEMINI_API_KEY // Gunakan API Key dari BuildConfig
        )

        // Setup tombol kirim
        sendButton.setOnClickListener {
            val userMessage = messageEditText.text.toString()
            if (userMessage.isNotBlank()) {
                sendMessage(userMessage)
                messageEditText.text.clear()
            }
        }

    }

    private fun sendMessage(userMessage: String) {
        // Tambahkan pesan pengguna ke UI
        addMessage(userMessage, true)

        // Gunakan lifecycleScope untuk menjalankan coroutine
        lifecycleScope.launch {
            try {
                // Tampilkan indikator loading (opsional)
                addMessage("Mengetik...", false)

                // Kirim pesan ke Gemini API
                val response = generativeModel.generateContent(
                    content("user") {
                        text(userMessage)
                    }
                )

                // Hapus indikator loading
                removeLastMessage()

                // Tampilkan respon dari bot
                response.text?.let { botResponse ->
                    addMessage(botResponse, false)
                }

            } catch (e: Exception) {
                // Hapus indikator loading
                removeLastMessage()
                // Tampilkan pesan error
                addMessage("Gagal mendapatkan respon: ${e.message}", false)
                Toast.makeText(this@ChatActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addMessage(text: String, isFromUser: Boolean) {
        messageList.add(Message(text, isFromUser))
        messageAdapter.notifyItemInserted(messageList.size - 1)
        chatRecyclerView.scrollToPosition(messageList.size - 1)
    }

    private fun removeLastMessage() {
        if (messageList.isNotEmpty()) {
            val lastIndex = messageList.size - 1
            messageList.removeAt(lastIndex)
            messageAdapter.notifyItemRemoved(lastIndex)
        }
    }
}