package com.example.niceshot.views

import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import coil.compose.AsyncImage


@Composable
fun ViewPhotoScreen(
    uri: String
) {
    var immersiveMode = false
    val view = LocalView.current
    SideEffect {
        showHideSystemUi(view, immersiveMode)
        immersiveMode = true
    }

    val scale = remember { mutableStateOf(1f) }

    val transX = remember {
        mutableStateOf(0f)
    }
    val transY = remember {
        mutableStateOf(0f)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .graphicsLayer(
                translationX = transX.value,
                translationY = transY.value,
                scaleX = scale.value,
                scaleY = scale.value,
            )
            .clickable {
                immersiveMode = !immersiveMode
                showHideSystemUi(view, immersiveMode)
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->

                    scale.value = when {
                        scale.value < 1f -> 1f
                        scale.value > 3f -> 3f
                        else -> scale.value * zoom
                    }
                    if (scale.value > 1f) {
                        transX.value = transX.value + (pan.x * scale.value)
                        transY.value = transY.value + (pan.y * scale.value)
                    } else {
                        transX.value = 0f
                        transY.value = 0f
                    }

                }
            }

    ) {
        AsyncImage(
            model = uri,
            contentDescription = "photo",
            modifier = Modifier
                .fillMaxSize()
                    .align(Alignment.Center)
                .graphicsLayer(
                    scaleX = maxOf(1f, minOf(3f, scale.value)),
                    scaleY = maxOf(1f, minOf(3f, scale.value)),
                )
        )
    }
}

fun showHideSystemUi(view: View, immersiveMode: Boolean) {
    val window = view.context.getActivity()!!.window
    if (immersiveMode) {
        WindowCompat.getInsetsController(window, view).show(
            WindowInsetsCompat.Type.statusBars() or
                    WindowInsetsCompat.Type.navigationBars()
        )
    } else {
        WindowCompat.getInsetsController(window, view).hide(
            WindowInsetsCompat.Type.statusBars() or
                    WindowInsetsCompat.Type.navigationBars()
        )
    }
}