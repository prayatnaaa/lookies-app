package com.prayatna.lookiesapp.utils

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.prayatna.lookiesapp.ui.theme.light_primary
import com.prayatna.lookiesapp.ui.theme.light_primaryContainer

object Constants {
    val gradientBackground = Brush.linearGradient(
        colors = listOf(
            light_primary,
            light_primaryContainer
        )
    )

    val glassBackground = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.25f),
            Color.White.copy(alpha = 0.1f)
        )
    )
}