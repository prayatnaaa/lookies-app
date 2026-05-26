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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
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
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            OutlinedButton(
                onClick = onReject,
                modifier = Modifier.weight(1f),
                enabled = !isDeciding
            ) {
                Text("Reject")
            }

            Button(
                onClick = onApprove,
                modifier = Modifier.weight(1f),
                enabled = !isDeciding
            ) {

                if (isDeciding) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Approve Event")
                }
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
                text =
                    "Provide a reason so the organizer can revise and resubmit the event.",
                style =
                    MaterialTheme.typography.bodyMedium,
                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant
            )

            OutlinedTextField(
                value = reason,
                onValueChange = onReasonChange,
                label = { Text("Reason") },
                placeholder = {
                    Text("Explain why this event is rejected")
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                supportingText = {
                    Text("${reason.length}/300")
                }
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
private fun RevenueRulesSection(
    rules: List<EventRevenueRules>
) {
    Column(
        modifier = Modifier.padding(vertical = 20.dp)
    ) {

        Text(
            text = "Revenue Distribution",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            rules.forEach { rule ->

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors =
                        CardDefaults.elevatedCardColors(
                            containerColor =
                                MaterialTheme.colorScheme.surface
                        )
                ) {

                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {

                        Text(
                            text = rule.itemType
                                .replace("_", " ")
                                .uppercase(),
                            style = MaterialTheme
                                .typography
                                .titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        RevenueSplitRow(
                            label = "Artist",
                            value = "${rule.artistPercent}%"
                        )

                        RevenueSplitRow(
                            label = "Event",
                            value = "${rule.eventPercent}%"
                        )

                        RevenueSplitRow(
                            label = "Platform",
                            value =
                                "${rule.platformPercent}%"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RevenueSplitRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme
                .typography
                .bodyMedium,
            color =
                MaterialTheme.colorScheme
                    .onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme
                .typography
                .bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
