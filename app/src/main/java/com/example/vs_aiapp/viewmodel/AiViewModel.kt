package com.example.vs_aiapp.viewmodel

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vs_aiapp.model.ChatModel
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.launch
import java.util.Locale

class AiViewModel() : ViewModel() {

    var data = mutableStateListOf<ChatModel>()
    var tts: TextToSpeech? = null


    val gemini = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = "AIzaSyAYx6Tv6PoVLP2zZJGXEm7J38AgzWVzzNQ"
    )

    fun getResponse(prompt: String) {
        if(prompt.isNotEmpty())
        {
            data.add(ChatModel(msg = prompt, replayBy = "User"))
            data.add(ChatModel(msg = "...", replayBy = "AI"))
            viewModelScope.launch {
                try {
                    val response: GenerateContentResponse = gemini.generateContent(prompt)
                    Log.i("TAG", "getResponse: ======= ${response.text}")
                    data[data.size - 1] = ChatModel(msg = response.text!!, replayBy = "AI")
                } catch (_: Exception) {
                    data[data.size - 1] = ChatModel(msg = "This content is blocked", replayBy = "AI")

                }

            }
        }
    }

    fun textToSpeech(context: Context,text:String){

        tts = TextToSpeech(
            context
        ) {
            if (it == TextToSpeech.SUCCESS) {
                tts?.let { txtToSpeech ->
                    txtToSpeech.language = Locale.US
                    txtToSpeech.setSpeechRate(1.0f)
                    txtToSpeech.speak(
                        text,
                        TextToSpeech.QUEUE_ADD,
                        null,
                        null
                    )
                }
            }

        }
    }

}