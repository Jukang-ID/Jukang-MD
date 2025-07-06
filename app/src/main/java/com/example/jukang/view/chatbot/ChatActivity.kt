package com.example.jukang.view.chatbot

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jukang.BuildConfig
import com.example.jukang.R
import com.example.jukang.databinding.ActivityChatBinding
import com.example.jukang.helper.Message
import com.example.jukang.helper.adapter.AdapterChat
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var generativeModel: GenerativeModel
    private val messageList = mutableListOf<com.example.jukang.helper.Message>()
    private lateinit var messageAdapter: AdapterChat
    private lateinit var chat: Chat
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = resources.getColor(R.color.primary_button)
        window.navigationBarColor = Color.WHITE

        // 3. Ubah ikon di status bar & navigation bar menjadi gelap.
        // Ini penting! Karena background-nya sekarang terang (putih), ikonnya harus gelap biar kelihatan.
        WindowCompat.getInsetsController(window, window.decorView).let { controller ->
            controller.isAppearanceLightStatusBars = true
            controller.isAppearanceLightNavigationBars = true
        }

        // Inisialisasi UI
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageEditText = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.sendButton)

        // Setup RecyclerView
        messageAdapter = AdapterChat(messageList)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        val systemInstruction =
            "Kamu adalah Jukang, asisten AI yang ramah, gaul, dan ahli dalam bidang pertukangan. Selalu gunakan bahasa yang santai dan mudah dimengerti seperti anak Gen Z. Berikan jawaban yang detail namun tetap asik dibaca."

        // Inisialisasi Model Gemini
        generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash-latest",
            apiKey = BuildConfig.GEMINI_API_KEY, // Gunakan API Key dari BuildConfig,
            systemInstruction = content {
                text(systemInstruction)
            }
        )

        chat = generativeModel.startChat()

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
                addMessage("Lagi ngetik...", false)

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
                Toast.makeText(
                    this@ChatActivity,
                    "Error: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
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