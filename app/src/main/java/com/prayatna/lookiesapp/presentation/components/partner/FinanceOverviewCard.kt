package com.prayatna.lookiesapp.presentation.components.partner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.utils.Constants

@Composable
fun FinancialOverviewCard(
    totalRevenue: String,
    salesGrowth: String,
    pendingPayout: String? = null
) {
    Card(
        shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Wallet,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Total Revenue",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = totalRevenue,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                if (pendingPayout != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Pending, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Pending Payout: $pendingPayout", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                } else {
                    Spacer(modifier = Modifier.height(8.dp))

                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardDoubleArrowUp,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = salesGrowth,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}