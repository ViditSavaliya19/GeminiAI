package com.example.vs_aiapp.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vs_aiapp.model.ChatModel
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.launch

class AiViewModel : ViewModel() {
//    private var _aiResponse = MutableLiveData<MutableList<String>>()
//    var data: LiveData<MutableList<String>> = _aiResponse


    var data= mutableStateListOf<ChatModel>()
    val gemini = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = "AIzaSyAYx6Tv6PoVLP2zZJGXEm7J38AgzWVzzNQ"
    )

     fun getResponse(prompt: String) {
         data.add(ChatModel(msg = prompt, replayBy = "User"))
         data.add(ChatModel(msg = "...", replayBy = "AI"))
         viewModelScope.launch {
            try {
                val response: GenerateContentResponse = gemini.generateContent(prompt   )
                Log.i("TAG", "getResponse: ======= ${response.text}")
                data[data.size-1] =ChatModel(msg = response.text!!, replayBy = "AI")
            }
            catch(_:Exception)
            {
                data[data.size-1] =ChatModel(msg = "This content is blocked", replayBy = "AI")

            }

         }
    }

}