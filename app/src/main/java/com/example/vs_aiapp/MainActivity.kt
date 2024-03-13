package com.example.vs_aiapp

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.vs_aiapp.view.AIScreen
import com.example.vs_aiapp.viewmodel.AiViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {
    val aiViewModel : AiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIScreen(aiViewModel)
        }
    }


}
