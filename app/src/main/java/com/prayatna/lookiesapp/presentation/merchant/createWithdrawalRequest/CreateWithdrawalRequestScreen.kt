package com.prayatna.lookiesapp.presentation.merchant.createWithdrawalRequest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.merchant.createWithdrawalRequest.state.CreateWithdrawalRequestEvent
import com.prayatna.lookiesapp.presentation.merchant.createWithdrawalRequest.state.CreateWithdrawalRequestUiState

@Composable
fun CreateWithdrawalRequestScreen(
    uiState: CreateWithdrawalRequestUiState,
    onEvent: (CreateWithdrawalRequestEvent) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = { onEvent(CreateWithdrawalRequestEvent.BackClicked) },
                title = "Create Withdrawal"
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Request a withdrawal to your bank account. Please ensure your bank details are correct.",
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = uiState.amount,
                onValueChange = { onEvent(CreateWithdrawalRequestEvent.AmountChanged(it)) },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.bankCode,
                onValueChange = { onEvent(CreateWithdrawalRequestEvent.BankCodeChanged(it)) },
                label = { Text("Bank Code (e.g. BCA, BNI)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.accountNumber,
                onValueChange = { onEvent(CreateWithdrawalRequestEvent.AccountNumberChanged(it)) },
                label = { Text("Account Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.accountName,
                onValueChange = { onEvent(CreateWithdrawalRequestEvent.AccountNameChanged(it)) },
                label = { Text("Account Holder Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onEvent(CreateWithdrawalRequestEvent.SubmitClicked) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularLoading()
                } else {
                    Text("Submit Request")
                }
            }
        }
    }
}
