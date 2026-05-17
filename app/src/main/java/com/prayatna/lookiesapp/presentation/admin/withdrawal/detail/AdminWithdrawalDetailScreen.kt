package com.prayatna.lookiesapp.presentation.admin.withdrawal.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.admin.withdrawal.detail.state.AdminWithdrawalDetailUiEvent
import com.prayatna.lookiesapp.presentation.admin.withdrawal.detail.state.AdminWithdrawalDetailUiState
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.utils.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminWithdrawalDetailScreen(
    uiState: AdminWithdrawalDetailUiState,
    onEvent: (AdminWithdrawalDetailUiEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    val request = uiState.withdrawalRequest ?: return

    var showRejectSheet by remember { mutableStateOf(false) }
    var showApproveSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            DetailItem(label = "Amount", value = formatRupiah(request.amount.toDouble()))
            DetailItem(label = "Status", value = request.status.replaceFirstChar { it.uppercase() })
            DetailItem(label = "Merchant ID", value = request.merchantId)
            DetailItem(label = "Bank Code", value = request.bankCode)
            DetailItem(label = "Account Number", value = request.accountNumber)
            DetailItem(label = "Account Holder", value = request.accountName)
            DetailItem(label = "Created At", value = request.createdAt)
            
            if (request.adminNotes != null) {
                DetailItem(label = "Admin Notes", value = request.adminNotes)
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (request.status == "pending") {
                OutlinedTextField(
                    value = uiState.notes,
                    onValueChange = {
                        onEvent(AdminWithdrawalDetailUiEvent.OnNotesChanged(
                            it
                        ))
                    },
                    label = { Text("Admin Notes (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { showApproveSheet = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Approve")
                    }

                    OutlinedButton(
                        onClick = { showRejectSheet = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Reject")
                    }
                }
            }
        }
    }

    if (showApproveSheet) {
        CustomBottomSheet(
            title = "Approve Payout",
            message = "Are you sure you want to approve and process this payout of ${formatRupiah(request.amount.toDouble())}?",
            confirmText = "Approve",
            onConfirm = {
                onEvent(AdminWithdrawalDetailUiEvent.ProcessPayout(request.id))
                showApproveSheet = false
            },
            onDismiss = { showApproveSheet = false }
        )
    }

    if (showRejectSheet) {
        CustomBottomSheet(
            title = "Reject Withdrawal",
            message = "Are you sure you want to reject this withdrawal request?",
            confirmText = "Reject",
            onConfirm = {
                onEvent(
                    AdminWithdrawalDetailUiEvent.UpdateStatus(
                        request.id,
                        "rejected",
                        uiState.notes.ifBlank { null }
                    )
                )
                showRejectSheet = false
            },
            onDismiss = { showRejectSheet = false }
        )
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}
