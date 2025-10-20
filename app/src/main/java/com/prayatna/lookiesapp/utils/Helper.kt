package com.prayatna.lookiesapp.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import com.prayatna.lookiesapp.BuildConfig
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity

object Helper {
    fun buildImageUrl(imageName: String, bucketName: String) =
        "${BuildConfig.BASE_URL}/storage/v1/object/public/${bucketName}/${imageName}"
}

@Composable
fun rememberImeState(): State<Boolean> {
    val density = LocalDensity.current
    val imeBottom = WindowInsets.ime.getBottom(density)
    val imeVisible = imeBottom > 0
    val imeState = remember { mutableStateOf(false) }

    LaunchedEffect(imeVisible) {
        imeState.value = imeVisible
    }

    return imeState
}