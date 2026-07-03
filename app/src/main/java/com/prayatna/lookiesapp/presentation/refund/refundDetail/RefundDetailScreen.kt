package com.prayatna.lookiesapp.presentation.refund.refundDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.domain.model.transaction.DetailRefund
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.refund.refundDetail.state.RefundDetailEvent
import com.prayatna.lookiesapp.presentation.refund.refundDetail.state.RefundDetailUiState
import com.prayatna.lookiesapp.utils.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefundDetailScreen(
    uiState: RefundDetailUiState,
    onEvent: (RefundDetailEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = "Refund Details"
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading && uiState.refund == null -> {
                    CircularLoading(modifier = Modifier.align(Alignment.Center))
                }
                uiState.errorMessage != null && uiState.refund == null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = uiState.errorMessage, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { uiState.refund?.id?.let { onEvent(RefundDetailEvent.LoadRefund(it)) } }) {
                            Text("Retry")
                        }
                    }
                }
                uiState.refund != null -> {
                    RefundContent(
                        refund = uiState.refund,
                        trackingNumber = uiState.trackingNumber,
                        isSubmitting = uiState.isSubmittingTracking,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@Composable
private fun RefundContent(
    refund: DetailRefund,
    trackingNumber: String,
    isSubmitting: Boolean,
    onEvent: (RefundDetailEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Status Card
        StatusCard(status = refund.status)

        // Basic Info Card
        InfoCard(
            title = "Refund Information",
            icon = Icons.Default.Receipt
        ) {
            InfoRow(label = "Amount", value = formatRupiah(refund.amount))
            InfoRow(label = "Reason", value = refund.reason)
            InfoRow(label = "Bank", value = "${refund.bankCode} - ${refund.accountNumber}")
            InfoRow(label = "Account Holder", value = refund.accountHolderName)
        }

        // Address Card
        if (!refund.streetLine1.isNullOrBlank()) {
            InfoCard(
                title = "Return Address",
                icon = Icons.Default.LocationOn
            ) {
                Text(
                    text = listOfNotNull(refund.streetLine1, refund.streetLine2).joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = listOfNotNull(refund.city, refund.provinceState, refund.postalCode).joinToString(", "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                refund.country?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Proof Section
        if (!refund.proofImageUrl.isNullOrEmpty()) {
            InfoCard(title = "Proof Image", icon = Icons.Default.Info) {
                AsyncImage(
                    model = refund.proofImageUrl.replace("http://172.21.179.110", "http://10.0.2.2"),
                    contentDescription = "Refund Proof",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Admin Notes
        if (!refund.adminNotes.isNullOrEmpty()) {
            InfoCard(title = "Admin Notes", icon = Icons.Default.Info) {
                Text(
                    text = refund.adminNotes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Tracking Number Section
        TrackingSection(
            refund = refund,
            trackingNumber = trackingNumber,
            isSubmitting = isSubmitting,
            onEvent = onEvent
        )
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun StatusCard(status: String) {
    val (color, label) = when (status) {
        "pending" -> MaterialTheme.colorScheme.primary to "Pending Review"
        "waiting_for_return" -> Color(0xFFFFA000) to "Waiting for Return"
        "returning" -> Color(0xFF2196F3) to "Returning to Artist"
        "return_received" -> Color(0xFF4CAF50) to "Return Received"
        "rejected" -> MaterialTheme.colorScheme.error to "Rejected"
        "completed" -> Color(0xFF4CAF50) to "Refund Completed"
        else -> MaterialTheme.colorScheme.outline to status.replaceFirstChar { it.uppercase() }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun TrackingSection(
    refund: DetailRefund,
    trackingNumber: String,
    isSubmitting: Boolean,
    onEvent: (RefundDetailEvent) -> Unit
) {
    val showInput = refund.status == "waiting_for_return"
    val showDisplay = !refund.returnTrackingNumber.isNullOrEmpty() || refund.status == "returning"

    if (showInput || showDisplay) {
        InfoCard(title = "Shipping Status", icon = Icons.Default.LocalShipping) {
            if (showInput) {
                Text(
                    text = "Please enter the courier tracking number to return the artwork.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = trackingNumber,
                    onValueChange = { onEvent(RefundDetailEvent.OnTrackingNumberChange(it)) },
                    label = { Text("Tracking Number") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !isSubmitting
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { onEvent(RefundDetailEvent.SubmitTrackingNumber) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = trackingNumber.isNotBlank() && !isSubmitting
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Submit Tracking Number")
                    }
                }
            } else {
                InfoRow(
                    label = "Tracking Number",
                    value = refund.returnTrackingNumber ?: "Updating..."
                )
            }
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            content()
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}
