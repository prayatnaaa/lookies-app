package com.prayatna.lookiesapp.presentation.components.shipment

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.components.CustomAsyncImage
import com.prayatna.lookiesapp.presentation.components.detailTransaction.OrderItemCard
import com.prayatna.lookiesapp.presentation.components.transactionList.TransactionStatusChip
import com.prayatna.lookiesapp.presentation.shipmentDetail.state.ShipmentDetailEvent
import com.prayatna.lookiesapp.presentation.shipmentDetail.state.ShipmentDetailUiState
import com.prayatna.lookiesapp.utils.formatDate
import com.prayatna.lookiesapp.utils.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShipmentDetailContent(
    uiState: ShipmentDetailUiState,
    onEvent: (ShipmentDetailEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val shipment = uiState.shipment
    val transactionDetail = uiState.transactionDetail
    var expanded by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            onEvent(ShipmentDetailEvent.OnArrivalProofSelected(it))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (shipment != null) {
            Text(text = "Shipment Info", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Order ID", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = shipment.orderId.take(8).uppercase(), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Created At", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = formatDate(shipment.createdAt), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            if (transactionDetail != null) {
                HorizontalDivider()
                Text(text = "Order Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Status", style = MaterialTheme.typography.bodyMedium)
                    TransactionStatusChip(status = transactionDetail.status)
                }

                transactionDetail.items.forEach { item ->
                    OrderItemCard(item = item)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total Amount", fontWeight = FontWeight.Bold)
                        Text(text = formatRupiah(transactionDetail.totalAmount), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            HorizontalDivider()

            Text(text = "Recipient Info", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Name: ${shipment.reciepentName}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Phone: ${shipment.phoneNumber}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Address: ${shipment.addressLine}\n${shipment.province}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            HorizontalDivider()

            Text(
                text = "Arrival Proof",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                onClick = {
                    imagePickerLauncher.launch("image/*")
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    when {
                        uiState.selectedArrivalProof != null -> {
                            CustomAsyncImage(
                                model = uiState.selectedArrivalProof,
                                contentDescription = "Selected Proof",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        !shipment.arrivalProofUrl.isNullOrBlank() -> {
                            CustomAsyncImage(
                                model = shipment.arrivalProofUrl,
                                contentDescription = "Arrival Proof",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // default kosong
                        else -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.CloudUpload,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    "Upload Arrival Proof",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    onEvent(ShipmentDetailEvent.SubmitArrivalProof)
                },
                enabled = !uiState.isUploadingProof &&
                        uiState.selectedArrivalProof != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isUploadingProof) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        if (!shipment.arrivalProofUrl.isNullOrBlank())
                            "Update Proof"
                        else
                            "Submit Proof"
                    )
                }
            }

            HorizontalDivider()

            Text(text = "Update Status", style = MaterialTheme.typography.titleMedium)
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = uiState.selectedStatus.replaceFirstChar { it.uppercase() },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Status") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    uiState.availableStatuses.forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status.replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                onEvent(ShipmentDetailEvent.OnStatusSelected(status))
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            Button(
                onClick = { onEvent(ShipmentDetailEvent.SubmitStatusUpdate) },
                enabled = !uiState.isUpdating,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Status")
            }

            HorizontalDivider()

            Text(text = "Update Tracking Number", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = uiState.trackingNumberInput ?: "",
                onValueChange = { onEvent(ShipmentDetailEvent.OnTrackingNumberChanged(it)) },
                label = { Text("Tracking Number") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Button(
                onClick = { onEvent(ShipmentDetailEvent.SubmitTrackingNumberUpdate) },
                enabled = !uiState.isUpdating && uiState.trackingNumberInput?.isNotBlank() == true,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Tracking Number")
            }
        }
    }
}
