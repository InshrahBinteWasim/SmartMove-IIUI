package com.example.smartmoveiiui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartmoveiiui.databinding.ActivityChatbotBinding
import com.example.smartmoveiiui.ui.adapter.ChatMessage
import com.example.smartmoveiiui.ui.adapter.ChatMessageAdapter

class ChatbotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatbotBinding
    private val messages = mutableListOf<ChatMessage>()
    private lateinit var adapter: ChatMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ChatMessageAdapter(messages)
        binding.rvChatMessages.layoutManager = LinearLayoutManager(this)
        binding.rvChatMessages.adapter = adapter

        binding.chatToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSendChat.setOnClickListener {
            val text = binding.chatInputEdit.text.toString().trim()
            if (text.isNotEmpty()) {
                addMessage(text, false)
                binding.chatInputEdit.setText("")
                generateBotResponse(text)
            }
        }

        // Welcome message
        addMessage("Hello! I am SmartMove AI. How can I help you today? You can ask about routes, schedules, or delays.", true)
    }

    private fun addMessage(text: String, isBot: Boolean) {
        messages.add(ChatMessage(text, isBot))
        adapter.notifyItemInserted(messages.size - 1)
        binding.rvChatMessages.scrollToPosition(messages.size - 1)
    }

    private fun generateBotResponse(userInput: String) {
        val input = userInput.lowercase()
        val response = when {
            input.contains("route") -> "We have 5 active routes covering Islamabad and Rawalpindi. You can check the 'Bus Schedule' section for details."
            input.contains("time") || input.contains("schedule") -> "Buses generally run from 7:30 AM to 4:00 PM. Specific timings are listed in the Schedule section."
            input.contains("delay") -> "Current delays are posted in the 'Announcements' section. Please check there for live updates."
            input.contains("lost") -> "If you lost something on a bus, please submit a query through the 'My Queries' section."
            input.contains("hi") || input.contains("hello") -> "Hi there! How can I assist you with your campus commute?"
            else -> "I'm sorry, I don't quite understand. Could you try rephrasing? You can ask about routes, timings, or lost items."
        }
        
        binding.rvChatMessages.postDelayed({
            addMessage(response, true)
        }, 1000)
    }
}
