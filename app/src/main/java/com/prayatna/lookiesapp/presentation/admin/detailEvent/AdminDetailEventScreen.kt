package com.prayatna.lookiesapp.presentation.admin.detailEvent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.event.EventRevenueRules
import com.prayatna.lookiesapp.presentation.admin.detailEvent.state.AdminDetailEventUiEvent
import com.prayatna.lookiesapp.presentation.admin.detailEvent.state.AdminDetailEventUiState
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.detailevent.DetailEventContent
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.ui.theme.Maroon

@Composable
fun AdminDetailEventScreen(
    uiState: AdminDetailEventUiState,
    onEvent: (AdminDetailEventUiEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    var isRejectSheetOpen by rememberSaveable { mutableStateOf(false) }
    var isApproveSheetOpen by rememberSaveable { mutableStateOf(false) }
    var rejectReason by rememberSaveable { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BackTopBar(
                onBackClick = onNavigateBack,
                title = "Admin - Event Detail"
            )
        },
        bottomBar = {
            uiState.event?.let { event ->
                if (event.status == "pending_validation") {
                    AdminDetailEventBottomBar(
                        isDeciding = uiState.isDeciding,
                        onApprove = { isApproveSheetOpen = true },
                        onReject = { isRejectSheetOpen = true }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularLoading()
                    }
                }

                uiState.event != null -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        DetailEventContent(
                            event = uiState.event,
                            paintings = emptyList(),
                            isUserArtist = false,
                            extraContent = {
                                if (uiState.revenueRules.isNotEmpty()) {
                                    RevenueRulesSection(rules = uiState.revenueRules)
                                }
                            },
                            onPaintingClick = {},
                            onPartnerClick = {}
                        )
                    }
                }

                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.errorMessage,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }

    if (isApproveSheetOpen) {
        CustomBottomSheet(
            title = "Approve Event",
            message = "Are you sure you want to approve this event?",
            confirmText = "Approve",
            onConfirm = {
                uiState.event?.id?.toIntOrNull()?.let { id ->
                    onEvent(AdminDetailEventUiEvent.ApproveEvent(id))
                }
                isApproveSheetOpen = false
            },
            onDismiss = { isApproveSheetOpen = false }
        )
    }

    if (isRejectSheetOpen) {
        RejectEventBottomSheet(
            reason = rejectReason,
            onReasonChange = { rejectReason = it },
            onDismiss = { isRejectSheetOpen = false },
            onConfirm = {
                uiState.event?.id?.toIntOrNull()?.let { id ->
                    onEvent(AdminDetailEventUiEvent.RejectEvent(id, rejectReason))
                }
                isRejectSheetOpen = false
                rejectReason = ""
            }
        )
    }
}

@Composable
private fun AdminDetailEventBottomBar(
    isDeciding: Boolean,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(
                onClick = onApprove,
                modifier = Modifier.weight(1f),
                enabled = !isDeciding,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (isDeciding) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Approve")
                }
            }

            OutlinedButton(
                onClick = onReject,
                modifier = Modifier.weight(1f),
                enabled = !isDeciding,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Maroon)
            ) {
                Text("Reject")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RejectEventBottomSheet(
    reason: String,
    onReasonChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Reject Event",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = reason,
                onValueChange = onReasonChange,
                label = { Text("Rejection Reason") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    enabled = reason.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Maroon,
                        contentColor = Color.White
                    )
                ) {
                    Text("Reject")
                }
            }
        }
    }
}

@Composable
private fun RevenueRulesSection(rules: List<EventRevenueRules>) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = "Revenue Splits",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        rules.forEach { rule ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = rule.itemType.uppercase(), fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Artist: ${rule.artistPercent}%")
                        Text("Event: ${rule.eventPercent}%")
                        Text("Platform: ${rule.platformPercent}%")
                    }
                }
            }
        }
    }
}
