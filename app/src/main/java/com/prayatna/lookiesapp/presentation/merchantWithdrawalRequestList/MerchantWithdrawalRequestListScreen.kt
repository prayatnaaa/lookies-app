package com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.withdrawal.WithdrawalRequest
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.error.ErrorScreen
import com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList.state.MerchantWithdrawalRequestListEvent
import com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList.state.MerchantWithdrawalRequestListUiState
import com.prayatna.lookiesapp.utils.formatRupiah

@Composable
fun MerchantWithdrawalRequestListScreen(
    uiState: MerchantWithdrawalRequestListUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (MerchantWithdrawalRequestListEvent) -> Unit
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onEvent(MerchantWithdrawalRequestListEvent.CreateWithdrawalClicked) },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Create") }
            )
        },
        topBar = {
            BackTopBar(
                onBackClick = { onEvent(MerchantWithdrawalRequestListEvent.BackClicked) },
                title = "Withdrawal Requests"
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularLoading()
                }

                uiState.errorMessage.isNotEmpty() -> {
                    ErrorScreen(
                        message = uiState.errorMessage,
                        onRetry = { /* Retry logic if needed, usually just reload */ }
                    )
                }

                uiState.withdrawalRequests.isEmpty() -> {
                    EmptyWithdrawalState()
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = 8.dp,
                            bottom = 24.dp
                        ),
                    ) {
                        itemsIndexed(
                            items = uiState.withdrawalRequests,
                            key = { _, item -> item.id }
                        ) { index, request ->

                            WithdrawalRequestItem(
                                request = request,
                                showDivider = index != uiState.withdrawalRequests.lastIndex,
                                onClick = {
                                    if (request.status == "pending") {
                                        onEvent(
                                            MerchantWithdrawalRequestListEvent.DetailClicked(
                                                request.id
                                            )
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WithdrawalRequestItem(
    request: WithdrawalRequest,
    showDivider: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {

        // Top row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatRupiah(request.amount.toDouble()),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            StatusChip(status = request.status)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Bank info
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "${request.bankCode.formatStatus()} - ${request.accountNumber}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = request.accountName,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 22.dp, top = 2.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Requested on: ${request.createdAt.take(10)}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (showDivider) {
            Spacer(modifier = Modifier.height(14.dp))
            // line separator
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
            )
        }
    }
}

@Composable
private fun StatusChip(status: String) {
    val containerColor = when (status.lowercase()) {
        "completed" -> MaterialTheme.colorScheme.primaryContainer
        "pending" -> MaterialTheme.colorScheme.secondaryContainer
        "rejected" -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    
    val contentColor = when (status.lowercase()) {
        "completed" -> MaterialTheme.colorScheme.onPrimaryContainer
        "pending" -> MaterialTheme.colorScheme.onSecondaryContainer
        "rejected" -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = status.uppercase(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EmptyWithdrawalState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No withdrawal requests found",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun String.formatStatus(): String {
    return replace("_", " ")
        .split(" ")
        .joinToString(" ") { word ->
            word.uppercase()
        }
}

