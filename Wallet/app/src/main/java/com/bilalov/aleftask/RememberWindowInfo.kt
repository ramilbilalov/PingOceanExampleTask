package com.bilalov.aleftask

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun rememberWindowInfo ():WindowInfo{
    val configuration = LocalConfiguration.current
    return WindowInfo(
        screenInfoWidth = when {
          configuration.screenWidthDp < 600 -> WindowInfo.WindowType.Compact
            configuration.screenWidthDp < 840 -> WindowInfo.WindowType.Compact
            else ->  WindowInfo.WindowType.Expanded
        },
        screenInfoHeight = when {
            configuration.screenWidthDp < 480 -> WindowInfo.WindowType.Compact
            configuration.screenWidthDp < 900 -> WindowInfo.WindowType.Compact
            else ->  WindowInfo.WindowType.Expanded
        },
        screenHeight = configuration.screenHeightDp.dp,
        screenWith = configuration.screenWidthDp.dp
        )
}
data class WindowInfo(
    val screenInfoWidth: WindowType,
    val screenInfoHeight: WindowType,
    val screenWith: Dp,
    val screenHeight: Dp,


    ){
    sealed class WindowType{
        object Compact: WindowType()
        object Medium : WindowType()
        object Expanded : WindowType()
    }
}