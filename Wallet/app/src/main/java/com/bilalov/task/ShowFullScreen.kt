package com.bilalov.task

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.window.Dialog

@Composable
fun ShowFullScreen(isFullScreenOpen: MutableState<Boolean>, painter: Painter) {

    if (isFullScreenOpen.value) {
        Dialog(
            onDismissRequest = { isFullScreenOpen.value = false }) { Surface(
                modifier = Modifier
                    .fillMaxSize(100f)
                    .background(Color.Black)
            ) {
                Image(painter = painter,
                    contentDescription = "FullScreenImg",
                    modifier = Modifier
                        .fillMaxSize(100f)
                        .background(Color.Black)
                        .clickable {
                            isFullScreenOpen.value = false
                        }
                )
            }
        }
    }
}