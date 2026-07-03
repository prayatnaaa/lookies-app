package com.prayatna.lookiesapp.presentation.partner.eventTransactions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.domain.model.transaction.PaidOrderItem
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.partner.eventTransactions.state.EventTransactionListEvent
import com.prayatna.lookiesapp.presentation.partner.eventTransactions.state.EventTransactionListUiState
import com.prayatna.lookiesapp.utils.formatDate
import com.prayatna.lookiesapp.utils.formatRupiah

@Composable
fun EventTransactionListScreen(
    uiState: EventTransactionListUiState,
    onEvent: (EventTransactionListEvent) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = { onEvent(EventTransactionListEvent.OnBackClicked) },
                title = "Event Transactions"
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
                uiState.isLoading && uiState.items.isEmpty() -> {
                    CircularLoading(modifier = Modifier.align(Alignment.Center))
                }
                uiState.errorMessage != null && uiState.items.isEmpty() -> {
                    Text(
                        text = uiState.errorMessage,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
                uiState.items.isEmpty() -> {
                    Text(
                        text = "No transactions found for this event.",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(uiState.items) { item ->
                            TransactionItemRow(
                                item = item,
                                onClick = { onEvent(EventTransactionListEvent.OnItemClicked(item.orderId)) }
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionItemRow(
    item: PaidOrderItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.buyerName ?: "Walk-in Customer",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.itemType.replace("_", " ").uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 0.5.sp
                )
            }
            Text(
                text = formatRupiah(item.subtotal),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Qty: ${item.quantity}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = formatDate(item.paidAt),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
