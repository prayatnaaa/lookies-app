package com.prayatna.lookiesapp.presentation.components.painting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun WaterMark(modifier: Modifier = Modifier, fontSize: TextUnit = 34.sp) {
    Box(modifier = modifier) {
        Text(
            text = "LOOKIES",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            letterSpacing = 6.sp,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.4f),
                    offset = Offset(1f, 1f),
                    blurRadius = 8f
                )
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .rotate(-35f)
        )
    }
}