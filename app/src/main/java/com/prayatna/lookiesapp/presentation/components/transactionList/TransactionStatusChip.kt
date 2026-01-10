package com.prayatna.lookiesapp.presentation.components.transactionList

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
import java.util.Locale

@Composable
fun TransactionStatusChip(status: String) {
    val (color, text) = when (status.lowercase()) {
        "awaiting_payment", "pending" -> Pair(Color(0xFFFFA000), "Waiting payment")
        "paid", "completed", "settled" -> Pair(Color(0xFF4CAF50), "Paid")
        "expired", "failed" -> Pair(Color(0xFFE53935), "Failed")
        else -> Pair(
            Color.Gray,
            status.replace("_", " ")
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
    }

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(4.dp),
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}