package com.prayatna.lookiesapp.utils

import androidx.compose.ui.graphics.Brush
import com.prayatna.lookiesapp.ui.theme.AmoledBlack
import com.prayatna.lookiesapp.ui.theme.GreyTextLight

object Constants {
    val gradientBackground = Brush.linearGradient(
        colors = listOf(
            AmoledBlack,
            GreyTextLight
        )
    )
}