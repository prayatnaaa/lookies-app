package com.prayatna.lookiesapp.presentation.monthlyFinancleList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
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
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (state.selectedTab == 0) {
                                items(state.orderSplits) { split ->
                                    PayoutLogCard(split = split)
                                }
                            } else {
                                items(state.balanceLogs) { log ->
                                    BalanceLogCard(log = log)
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
private fun PayoutLogCard(split: OrderSplit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order #${split.orderId.take(8)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = split.payoutStatus.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (split.payoutStatus == "completed") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            FinancialRowItem(label = "Gross Amount", value = "Rp ${split.grossAmount.toLong()}")
            FinancialRowItem(label = "Platform Fee", value = "- Rp ${split.platformFee.toLong()}")
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Net Amount",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Rp ${split.netAmount.toLong()}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun BalanceLogCard(log: MerchantBalanceLog) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = log.transactionType.replace("_", " ").uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (log.amount > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
                Text(
                    text = log.createdAt.take(10),
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (log.amount > 0) "+ Rp ${log.amount}" else "- Rp ${-log.amount}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (log.amount > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )

            log.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Balance after: Rp ${log.balanceAfter}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
