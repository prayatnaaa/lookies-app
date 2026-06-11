package com.prayatna.lookiesapp.presentation.monthlyFinancleList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.transaction.MerchantBalanceLog
import com.prayatna.lookiesapp.domain.model.transaction.OrderSplit
import com.prayatna.lookiesapp.presentation.monthlyFinancleList.state.MonthlyFinanceEvent
import com.prayatna.lookiesapp.presentation.monthlyFinancleList.state.MonthlyFinanceUiState
import com.prayatna.lookiesapp.utils.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyFinanceListScreen(
    state: MonthlyFinanceUiState,
    onEvent: (MonthlyFinanceEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Financial Records", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(MonthlyFinanceEvent.NavigateBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            if (state.selectedTab == 1) {
                ExtendedFloatingActionButton(
                    onClick = { onEvent(MonthlyFinanceEvent.WithdrawalListClicked) },
                    icon = { Icon(Icons.Default.History, contentDescription = null) },
                    text = { Text("Withdrawal History") }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = state.selectedTab,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = state.selectedTab == 0,
                    onClick = { onEvent(MonthlyFinanceEvent.TabSelected(0)) },
                    text = { Text("Payout Logs") }
                )
                Tab(
                    selected = state.selectedTab == 1,
                    onClick = { onEvent(MonthlyFinanceEvent.TabSelected(1)) },
                    text = { Text("Balance History") }
                )
            }

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (state.errorMessage != null) {
                    ErrorState(message = state.errorMessage)
                } else {
                    val currentList = if (state.selectedTab == 0) state.orderSplits else state.balanceLogs
                    
                    if (currentList.isEmpty()) {
                        EmptyFinanceState(
                            message = if (state.selectedTab == 0) "No payout logs found" else "No balance history found"
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            if (state.selectedTab == 0) {
                                itemsIndexed(state.orderSplits) { index, split ->
                                    PayoutLogItem(
                                        split = split,
                                        showDivider = index != state.orderSplits.lastIndex,
                                        onClick = { onEvent(MonthlyFinanceEvent.PayoutLogClicked(split.orderId)) }
                                    )
                                }
                            } else {
                                itemsIndexed(state.balanceLogs) { index, log ->
                                    BalanceLogItem(
                                        log = log,
                                        showDivider = index != state.balanceLogs.lastIndex
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PayoutLogItem(
    split: OrderSplit,
    showDivider: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Net Amount",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = formatRupiah(split.netAmount),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = split.payoutStatus.formatStatus(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = when (split.payoutStatus) {
                    "on_hold" -> {
                        MaterialTheme.colorScheme.primary
                    }
                    "pending" -> {
                        MaterialTheme.colorScheme.secondary
                    }
                    "completed", "settled", "paid" -> {
                        MaterialTheme.colorScheme.tertiary
                    }
                    else -> {
                        MaterialTheme.colorScheme.error
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        FinancialRowItem(
            label = "Gross Amount",
            value = formatRupiah(split.grossAmount)
        )

        FinancialRowItem(
            label = "Platform Fee",
            value = "- ${formatRupiah(split.platformFee)}"
        )

        if (showDivider) {
            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
            )
        }
    }
}
@Composable
private fun BalanceLogItem(
    log: MerchantBalanceLog,
    showDivider: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = log.transactionType
                        .replace("_", " ")
                        .uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (log.amount > 0) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )

                log.description?.let {
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = log.createdAt.take(10),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (log.amount > 0) {
                "+ ${formatRupiah(log.amount.toDouble())}"
            } else {
                "- ${formatRupiah((-log.amount).toDouble())}"
            },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = if (log.amount > 0) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.error
            }
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Balance after: ${
                formatRupiah(log.balanceAfter.toDouble())
            }",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (showDivider) {
            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
            )
        }
    }
}
@Composable
private fun FinancialRowItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
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

@Composable
private fun BoxScope.EmptyFinanceState(message: String) {
    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Receipt,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No records found",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

private fun String.formatStatus(): String {
    return replace("_", " ")
        .split(" ")
        .joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { it.uppercase() }
        }
}

@Composable
private fun BoxScope.ErrorState(message: String) {
    Text(
        text = message,
        modifier = Modifier
            .align(Alignment.Center)
            .padding(32.dp),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.error
    )
}
