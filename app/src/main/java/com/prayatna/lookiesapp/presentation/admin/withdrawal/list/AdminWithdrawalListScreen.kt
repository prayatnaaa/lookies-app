package com.prayatna.lookiesapp.presentation.admin.withdrawal.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.withdrawal.WithdrawalRequest
import com.prayatna.lookiesapp.presentation.admin.withdrawal.list.state.AdminWithdrawalListUiEvent
import com.prayatna.lookiesapp.presentation.admin.withdrawal.list.state.AdminWithdrawalListUiState
import com.prayatna.lookiesapp.utils.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminWithdrawalListScreen(
    uiState: AdminWithdrawalListUiState,
    onEvent: (AdminWithdrawalListUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Withdrawal Requests") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.withdrawalRequests.isEmpty()) {
                Text(
                    text = "No withdrawal requests",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.withdrawalRequests) { request ->
                        WithdrawalItem(
                            request = request,
                            onClick = {
                                onNavigateToDetail(request.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WithdrawalItem(
    request: WithdrawalRequest,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatRupiah(request.amount.toDouble()),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                StatusBadge(status = request.status)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Account: ${request.accountName}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Bank: ${request.bankCode} - ${request.accountNumber}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val color = when (status) {
        "pending" -> Color(0xFFFFA000)
        "approved", "completed" -> Color(0xFF388E3C)
        "rejected" -> Color(0xFFD32F2F)
        else -> MaterialTheme.colorScheme.onSurface
    }
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small,
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Text(
            text = status.replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            color = color,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
