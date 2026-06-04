package com.prayatna.lookiesapp.presentation.partner.partnerRefund

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.domain.model.transaction.DetailRefund
import com.prayatna.lookiesapp.presentation.partner.partnerRefund.state.PartnerRefundEvent
import com.prayatna.lookiesapp.presentation.partner.partnerRefund.state.PartnerRefundUiState
import com.prayatna.lookiesapp.utils.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerRefundScreen(
    state: PartnerRefundUiState,
    onEvent: (PartnerRefundEvent) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val refund = state.data

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Refund Detail") },
                navigationIcon = {
                    IconButton(
                        onClick = { onEvent(PartnerRefundEvent.BackClicked) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            if (state.isLoading && refund == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                if (state.isLoading && refund != null) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                refund?.let { r ->

                    HeaderSection(r)

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!r.proofImageUrl.isNullOrBlank()) {
                        ProofImageSection(r.proofImageUrl)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    ActionSection(
                        refund = r,
                        notes = state.notes,
                        isLoading = state.isLoading,
                        onNotesChanged = { onEvent(PartnerRefundEvent.NotesChanged(it)) },
                        onActionClicked = { newStatus ->
                            onEvent(PartnerRefundEvent.StatusChanged(newStatus))

                            if (newStatus == "completed") {
                                onEvent(PartnerRefundEvent.ProcessClicked)
                            } else {
                                onEvent(PartnerRefundEvent.UpdateClicked)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    DetailCard(r)
                }

                state.error?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    refund: DetailRefund
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Current Status: ${refund.status.uppercase()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Amount: ${formatRupiah(refund.amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ProofImageSection(
    url: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Text(
                text = "Proof Image",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            AsyncImage(
                model = url.replace("http://172.21.179.110", "http://10.0.2.2"),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.4f)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Fit,
                placeholder = null,
                error = null
            )
        }
    }
}

@Composable
private fun DetailCard(
    refund: DetailRefund
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Refund Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoRow("Order ID", refund.orderId)
            InfoRow("User ID", refund.userId)
            InfoRow("Bank", refund.bankCode)
            InfoRow("Account Number", refund.accountNumber)
            InfoRow("Account Name", refund.accountHolderName)
            InfoRow("Reason", refund.reason)
            InfoRow("Admin Notes", refund.adminNotes ?: "-")
            InfoRow("Created", refund.createdAt ?: "-")
            InfoRow("Updated", refund.updatedAt ?: "-")
        }
    }
}

@Composable
private fun InfoRow(
    title: String,
    value: String
) {
    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = value,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun ActionSection(
    refund: DetailRefund,
    notes: String?,
    isLoading: Boolean,
    onNotesChanged: (String) -> Unit,
    onActionClicked: (newStatus: String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Action",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = notes.orEmpty(),
                onValueChange = onNotesChanged,
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                label = { Text("Notes") },
                enabled = !isLoading && (refund.status == "pending" || refund.status == "returning")
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (refund.status) {
                "pending" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { onActionClicked("rejected") },
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Refuse")
                        }

                        Button(
                            onClick = { onActionClicked("waiting_for_return") },
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading
                        ) {
                            Text("Accept and return")
                        }
                    }
                }

                "waiting_for_return" -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Waiting for customer to return and input return tracking number",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                "returning" -> {
                    refund.returnTrackingNumber?.let { no ->
                        Text(
                            text = "No. tracking number: $no",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    Button(
                        onClick = { onActionClicked("completed") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        Text("Confirm return and refund")
                    }
                }

                "rejected", "completed" -> {
                    Text(
                        text = "This refund process is closed with (${refund.status.uppercase()}).",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
