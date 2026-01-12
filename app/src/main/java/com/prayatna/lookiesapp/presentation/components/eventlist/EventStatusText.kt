package com.prayatna.lookiesapp.presentation.components.eventlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class EventStatus(val value: String) {
    PendingValidation("pending_validation"),
    Published("published"),
    Ongoing("ongoing"),
    Completed("completed"),
    Cancelled("cancelled"),
    Unknown("unknown");

    companion object {
        fun from(value: String): EventStatus =
            entries.find { it.value == value } ?: Unknown
    }
}

@Composable
fun EventStatusText(
    status: String,
    modifier: Modifier = Modifier
) {
    val eventStatus = EventStatus.from(status)

    val (text, color) = when (eventStatus) {
        EventStatus.PendingValidation -> "Pending" to Color(0xFFFFC107) // Amber
        EventStatus.Published -> "Published" to Color(0xFF4CAF50) // Green
        EventStatus.Ongoing -> "Ongoing" to MaterialTheme.colorScheme.surface
        EventStatus.Completed -> "Completed" to Color(0xFF2196F3) // Blue
        EventStatus.Cancelled -> "Cancelled" to Color.Gray
        EventStatus.Unknown -> "Unknown" to Color.LightGray
    }

    Text(
        text = text,
        color = color,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}
