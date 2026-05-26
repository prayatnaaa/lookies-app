package com.prayatna.lookiesapp.presentation.admin.transactionList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.admin.AdminTransaction
import com.prayatna.lookiesapp.presentation.admin.transactionList.state.AdminTransactionListUiEvent
import com.prayatna.lookiesapp.presentation.admin.transactionList.state.AdminTransactionListUiState
import com.prayatna.lookiesapp.utils.formatDate
import com.prayatna.lookiesapp.utils.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTransactionListScreen(
    uiState: AdminTransactionListUiState,
    onEvent: (AdminTransactionListUiEvent) -> Unit,
    snackbarHostState: SnackbarHostState
) {

    val statuses = listOf(
        null,
        "paid",
        "refunded",
        "cancelled",
        "awaiting_shipment",
        "shipped",
        "completed"
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Transactions",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onEvent(
                                AdminTransactionListUiEvent.OnBackClicked
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // Filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                statuses.forEach { status ->

                    val label = status
                        ?.replace("_", " ")
                        ?.replaceFirstChar { it.uppercase() }
                        ?: "All"

                    AssistChip(
                        onClick = {
                            onEvent(
                                AdminTransactionListUiEvent
                                    .OnStatusFilterChanged(status)
                            )
                        },
                        label = {
                            Text(label)
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor =
                                if (uiState.selectedStatus == status) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                }
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                when {

                    uiState.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    uiState.errorMessage != null -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.errorMessage,
                                color = MaterialTheme.colorScheme.error
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Retry",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable {
                                    onEvent(
                                        AdminTransactionListUiEvent
                                            .RefreshTransactions
                                    )
                                }
                            )
                        }
                    }

                    uiState.transactions.isEmpty() -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "No transactions",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                horizontal = 16.dp,
                                vertical = 12.dp
                            )
                        ) {
                            items(
                                items = uiState.transactions,
                                key = { it.id }
                            ) { transaction ->

                                AdminTransactionItem(
                                    transaction = transaction,
                                    onClick = {
                                        onEvent(
                                            AdminTransactionListUiEvent
                                                .OnTransactionClicked(
                                                    transaction.id
                                                )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminTransactionItem(
    transaction: AdminTransaction,
    onClick: () -> Unit
) {

    val statusColor = when (
        transaction.status.lowercase()
    ) {
        "created" -> MaterialTheme.colorScheme.tertiary
        "paid" -> MaterialTheme.colorScheme.primary
        "awaiting_shipment" -> Color(0xFFFF8F00)
        "shipped" -> Color(0xFF1565C0)
        "completed" -> Color(0xFF2E7D32)
        "cancelled" -> MaterialTheme.colorScheme.error
        "refunded" -> Color(0xFF6A1B9A)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "#${transaction.id.take(8)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formatDate(transaction.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = formatRupiah(transaction.totalAmount),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Box(
                modifier = Modifier
                    .background(
                        color = statusColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(999.dp)
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    )
            ) {
                Text(
                    text = transaction.status
                        .replace("_", " ")
                        .replaceFirstChar { it.uppercase() },
                    color = statusColor,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}