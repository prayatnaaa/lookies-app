package com.prayatna.lookiesapp.presentation.components.eventlist

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    modifier: Modifier = Modifier,
    isPublishTextToHome: Boolean = false
) {
    val eventStatus = EventStatus.from(status)

    val (text, color) = when (eventStatus) {
        EventStatus.PendingValidation -> "Pending" to Color(0xFFFFC107)
        EventStatus.Published -> (if (isPublishTextToHome) "Starting soon" else "Published") to Color(0xFF4CAF50)
        EventStatus.Ongoing -> "Ongoing" to Color(0xFF4CAF50)
        EventStatus.Completed -> "Completed" to Color(0xFF2196F3)
        EventStatus.Cancelled -> "Cancelled" to Color.Gray
        EventStatus.Unknown -> "Unknown" to Color.LightGray
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
//        Text(
//            text = "●",
//            color = color,
//            fontSize = 12.sp
//        )
//
//        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = text,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.labelMedium
        )
    }
}