package com.example.gemichat

import android.util.Log
import android.view.textclassifier.ConversationActions.Message
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    val generativeModel : GenerativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = Constants.apiKey
    )

    fun sendMessage(question : String){
        viewModelScope.launch {
            val chat = generativeModel.startChat(
                history = messageList.map {
                    content(it.role){text(it.message)}
                }.toList()
            )

            messageList.add(MessageModel(question, "User"))

            val response = chat.sendMessage(question)
            messageList.add(MessageModel(response.text.toString(), "model"))
            Log.i("Response from Gemini", response.text.toString())
        }
    }
}