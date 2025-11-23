package com.prayatna.lookiesapp.presentation.components.partner

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatusPill(text: String) {
    val backgroundColor = when (text.lowercase()) {
        "pending" -> Color(0xFFFFF4E6)
        "approved" -> Color(0xFFE6F4EA)
        "rejected" -> Color(0xFFFDEEED)
        else -> MaterialTheme.colorScheme.secondaryContainer
    }
    val textColor = when (text.lowercase()) {
        "pending" -> Color(0xFFE69B00)
        "approved" -> Color(0xFF00B031)
        "rejected" -> Color(0xFFD92D20)
        else -> MaterialTheme.colorScheme.onSecondaryContainer
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
    ) {
        Text(
            text = text.replaceFirstChar { it.titlecase() },
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}