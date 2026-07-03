package com.prayatna.lookiesapp.presentation.admin.transactionDetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.admin.AdminTransactionDetail
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.admin.transactionDetail.state.AdminTransactionDetailUiEvent
import com.prayatna.lookiesapp.presentation.admin.transactionDetail.state.AdminTransactionDetailUiState
import com.prayatna.lookiesapp.utils.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTransactionDetailScreen(
    uiState: AdminTransactionDetailUiState,
    onEvent: (AdminTransactionDetailUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = { onEvent(AdminTransactionDetailUiEvent.OnBackClicked) },
                title = "Transaction Detail"
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading && uiState.transaction == null -> {
                    CircularLoading(modifier = Modifier.align(Alignment.Center))
                }
                uiState.errorMessage != null && uiState.transaction == null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = uiState.errorMessage, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { onEvent(AdminTransactionDetailUiEvent.Refresh) }) {
                            Text("Retry")
                        }
                    }
                }
                uiState.transaction != null -> {
                    TransactionDetailContent(transaction = uiState.transaction)
                }
            }
        }
    }
}

@Composable
private fun TransactionDetailContent(transaction: AdminTransactionDetail) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Summary Header
        SummaryCard(transaction)

        // Customer Info
        InfoSection(
            title = "Customer Information",
            icon = Icons.Default.Person
        ) {
            InfoRow(label = "Name", value = transaction.userFullName ?: "N/A")
            InfoRow(label = "Email", value = transaction.userEmail ?: "N/A")
        }

        // Order Items
        InfoSection(
            title = "Order Items",
            icon = Icons.Default.ShoppingCart
        ) {
            transaction.items.forEach { item ->
                ItemRow(
                    name = if (item.itemType == "event_ticket") "Ticket for Event" else "Painting",
                    quantity = item.quantity,
                    price = item.unitPrice,
                    subtotal = item.subtotal
                )
                if (transaction.items.last() != item) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.surfaceVariant)
                }
            }
        }

        // Revenue Splits
        if (transaction.splits.isNotEmpty()) {
            InfoSection(
                title = "Revenue Distribution",
                icon = Icons.Default.AccountBalanceWallet
            ) {
                transaction.splits.forEach { split ->
                    SplitRow(
                        merchantId = split.merchantId,
                        net = split.netAmount,
                        fee = split.platformFee,
                        status = split.payoutStatus
                    )
                }
            }
        }

        // Payment Attempts
        if (transaction.paymentAttempts.isNotEmpty()) {
            InfoSection(
                title = "Payment History",
                icon = Icons.Default.History
            ) {
                transaction.paymentAttempts.forEach { attempt ->
                    PaymentAttemptRow(
                        channel = attempt.channel,
                        status = attempt.status,
                        date = attempt.createdAt
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SummaryCard(transaction: AdminTransactionDetail) {
    val statusColor = when (transaction.status.lowercase()) {
        "completed", "paid" -> Color(0xFF4CAF50)
        "pending", "awaiting_payment" -> Color(0xFFFFA000)
        "failed", "cancelled" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.outline
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Order ID",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "#${transaction.id.take(8).uppercase()}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = transaction.status.uppercase(),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Total Amount",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = formatRupiah(transaction.totalAmount),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun InfoSection(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
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
            Spacer(modifier = Modifier.height(16.dp))
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
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun ItemRow(name: String, quantity: Int, price: Double, subtotal: Double) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "$quantity x ${formatRupiah(price)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = formatRupiah(subtotal),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SplitRow(merchantId: String, net: Double, fee: Double, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (status == "paid") Color(0xFF4CAF50) else Color(0xFFFFA000))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Merchant ID: ${merchantId.take(8)}...", style = MaterialTheme.typography.bodySmall)
            Text(text = "Fee: ${formatRupiah(fee)}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(text = formatRupiah(net), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun PaymentAttemptRow(channel: String, status: String, date: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = channel.uppercase(), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(text = date.take(10), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(
            text = if (status == "PAID" || status == "paid") "Success" else status.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = if (status == "PAID" || status == "paid") Color(0xFF4CAF50) else MaterialTheme.colorScheme.error
        )
    }
}
