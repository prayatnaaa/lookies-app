package com.prayatna.lookiesapp.presentation.admin.detailEvent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.model.event.EventRevenueRules
import com.prayatna.lookiesapp.presentation.admin.detailEvent.state.AdminDetailEventUiEvent
import com.prayatna.lookiesapp.presentation.admin.detailEvent.state.AdminDetailEventUiState
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.detailevent.DetailEventContent
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.ui.theme.Maroon
import com.prayatna.lookiesapp.utils.formatDate
import com.prayatna.lookiesapp.utils.formatRupiah

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
                title = "Event Review"
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
                            showStatus = true,
                            extraContent = {
                                Column(
                                    modifier = Modifier.padding(bottom = 32.dp),
                                    verticalArrangement = Arrangement.spacedBy(32.dp)
                                ) {
                                    AdminQuotasSection(event = uiState.event)

                                    if (uiState.revenueRules.isNotEmpty()) {
                                        RevenueRulesSection(rules = uiState.revenueRules)
                                    }

                                    AdminAuditSection(event = uiState.event)
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
            message = "Are you sure you want to approve this event? This will make it visible to artists and buyers.",
            confirmText = "Confirm Approval",
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
        modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
        tonalElevation = 2.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedButton(
                onClick = onReject,
                modifier = Modifier.weight(1f).height(48.dp),
                enabled = !isDeciding,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Maroon.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Maroon)
            ) {
                Text("Reject", fontWeight = FontWeight.SemiBold)
            }

            Button(
                onClick = onApprove,
                modifier = Modifier.weight(1.5f).height(48.dp),
                enabled = !isDeciding,
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {

                if (isDeciding) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Approve Event", fontWeight = FontWeight.Bold)
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
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Reject Event Application",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Explain why this event doesn't meet the requirements. This feedback will be sent to the organizer.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            OutlinedTextField(
                value = reason,
                onValueChange = onReasonChange,
                label = { Text("Rejection Reason") },
                placeholder = { Text("e.g. Incomplete description, inappropriate banner...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                shape = RoundedCornerShape(16.dp),
                supportingText = {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text("${reason.length}/300")
                    }
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1.5f).height(48.dp),
                    enabled = reason.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Maroon,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Reject Application", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AdminQuotasSection(event: Event) {
    Column {
        SectionHeader(title = "Capacities & Fees", icon = Icons.Default.Inventory2)
        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Quotas Grid
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuotaCard(
                    label = "Participants",
                    total = event.maxParticipant?.toString() ?: "∞",
                    remaining = event.remainingParticipantQuota?.toString() ?: "-",
                    modifier = Modifier.weight(1f)
                )
                QuotaCard(
                    label = "Artworks",
                    total = event.maxPainting?.toString() ?: "∞",
                    remaining = event.remainingPaintingQuota?.toString() ?: "-",
                    modifier = Modifier.weight(1f)
                )
            }

            // Fee Items
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    MinimalAuditItem(
                        label = "Visitor Ticket",
                        value = event.ticketPrice?.let { if (it == 0.0) "Free" else formatRupiah(it) } ?: "N/A"
                    )
                    MinimalAuditItem(
                        label = "Artist Reg. Fee",
                        value = event.artistRegistrationFee?.let { if (it == 0.0) "Free" else formatRupiah(it) } ?: "N/A"
                    )
                    MinimalAuditItem(
                        label = "Limit per Artist",
                        value = "${event.maxPaintingPerArtist ?: "No Limit"} items"
                    )
                }
            }
        }
    }
}

@Composable
private fun QuotaCard(label: String, total: String, remaining: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = remaining, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(text = "/$total", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 4.dp))
            }
            Text(text = "Remaining", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AdminAuditSection(event: Event) {
    Column {
        SectionHeader(title = "Audit & System", icon = Icons.Default.Analytics)
        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                MinimalAuditItem(label = "Internal ID", value = "#${event.id.take(8).uppercase()}")
                MinimalAuditItem(label = "Classification", value = "${event.eventType.name} • ${event.eventFormat.name}")
                
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                MinimalAuditItem(label = "Timeline", value = "Created: ${formatDate(event.createdAt)}")

                event.updatedAt?.let {
                    MinimalAuditItem(label = "Last Activity", value = formatDate(it))
                }

                if (!event.approvedAt.isNullOrBlank()) {
                    MinimalAuditItem(label = "Approved", value = "${formatDate(event.approvedAt)} by ${event.approvedBy ?: "System"}")
                }

                if (event.locationUrl.isNotBlank()) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "Raw Location URL", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = event.locationUrl,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(8.dp),
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                if (!event.rejectionReason.isNullOrBlank()) {
                    Surface(
                        color = Maroon.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Maroon.copy(alpha = 0.1f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ErrorOutline, null, tint = Maroon, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(text = "Previous Rejection Reason", style = MaterialTheme.typography.labelSmall, color = Maroon, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = event.rejectionReason, style = MaterialTheme.typography.bodySmall, color = Maroon)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun MinimalAuditItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun RevenueRulesSection(rules: List<EventRevenueRules>) {
    Column {
        SectionHeader(title = "Revenue Distribution", icon = Icons.Default.Payments)
        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            rules.forEach { rule ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = rule.itemType.replace("_", " ").uppercase(),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.secondary,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                            RevenueMetric(label = "Artist", value = "${rule.artistPercent}%", modifier = Modifier.weight(1f))
                            RevenueMetric(label = "Event", value = "${rule.eventPercent}%", modifier = Modifier.weight(1f))
                            RevenueMetric(label = "Platform", value = "${rule.platformPercent}%", modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RevenueMetric(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    }
}
