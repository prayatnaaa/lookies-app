package com.prayatna.lookiesapp.utils

import androidx.compose.ui.graphics.Brush
import com.prayatna.lookiesapp.ui.theme.light_primary
import com.prayatna.lookiesapp.ui.theme.light_primaryContainer

object Constants {
    val gradientBackground = Brush.linearGradient(
        colors = listOf(
            light_primary,
            light_primaryContainer
        )
    )
}