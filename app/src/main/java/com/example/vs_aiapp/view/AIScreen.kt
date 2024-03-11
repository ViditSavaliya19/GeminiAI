package com.example.vs_aiapp.view

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.media.Image
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.vs_aiapp.MainActivity
import com.example.vs_aiapp.R
import com.example.vs_aiapp.ui.theme.Blue
import com.example.vs_aiapp.ui.theme.DarkBlue
import com.example.vs_aiapp.ui.theme.MateBlack
import com.example.vs_aiapp.viewmodel.AiViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AIScreen(aiViewModel: AiViewModel) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val lazyColumnListState = rememberLazyListState()
    val coroutineScope1 = rememberCoroutineScope()
    val context = LocalContext.current
    val tts: TextToSpeech? = null

    val responseList = aiViewModel.data.toList()
    var text by remember {
        mutableStateOf("")
    }




    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    "VS AI",
                    style = TextStyle(color = Color.White),
                )
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MateBlack),
        )
    }, containerColor = MateBlack) {

        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier.padding(top = 60.dp)
            ) {
                LazyColumn(
                    Modifier.weight(1f),
                    state = lazyColumnListState,
                    content = {
                        items(responseList.size) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            ) {
                                Box(modifier = Modifier
                                    .align(if (responseList[it].replayBy == "User") Alignment.CenterEnd else Alignment.CenterStart)
                                    .clip(shape = RoundedCornerShape(20f))
                                    .fillMaxWidth(0.7f)
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            listOf(DarkBlue, Blue)
                                        ),
                                    )
                                    .combinedClickable(onLongClick = {
                                        copy(context, responseList.get(it).msg)
                                        Toast
                                            .makeText(context, "Copy", Toast.LENGTH_SHORT)
                                            .show()
                                    }) {}

                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            responseList[it].msg
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            IconButton(onClick = {
                                                tts?.shutdown()
                                                tts?.speak(
                                                    responseList.get(it).msg,
                                                    TextToSpeech.QUEUE_FLUSH,
                                                    null,
                                                    null
                                                )
                                            }, content = {
                                                Image(
                                                    imageVector = ImageVector.vectorResource(R.drawable.baseline_volume_up_24),
                                                    contentDescription = "",
                                                    Modifier
                                                        .width(20.dp)
                                                        .height(20.dp)

                                                )
                                            })
                                            Text(
                                                responseList[it].replayBy,
                                                modifier = Modifier.fillMaxWidth(),
                                                style = TextStyle(fontWeight = FontWeight.Bold),
                                                textAlign = if (responseList[it].replayBy == "User") TextAlign.Right else TextAlign.Left
                                            )

                                        }
                                    }
                                }
                            }
                        }
                    },
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 20.dp, start = 10.dp, end = 10.dp)
                        .fillMaxWidth()
                ) {
                    TextField(
                        value = text,
                        onValueChange = {
                            text = it
                        },
                        modifier = Modifier
                            .weight(1f)
                            .bringIntoViewRequester(bringIntoViewRequester)
                            .onFocusEvent { focusState ->
                                if (focusState.isFocused) {
                                    coroutineScope1.launch {
                                        bringIntoViewRequester.bringIntoView()
                                        lazyColumnListState.scrollToItem(responseList.size)
                                    }
                                }
                            },
                        label = { Text("Prompt") },
                    )
                    FilledTonalIconButton(onClick = {
                        aiViewModel.getResponse(text)
                        text = ""
                    }, content = { Icon(Icons.Filled.Send, contentDescription = "") })

                }
            }
        }

    }
}

private fun copy(context: Context, txt: String) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("copy", txt)
    clipboardManager.setPrimaryClip(clip)

}

