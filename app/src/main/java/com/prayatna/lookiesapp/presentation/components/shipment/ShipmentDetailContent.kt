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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
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
                    if (!shipment.trackingNumber.isNullOrBlank()) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Tracking Number", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = shipment.trackingNumber, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
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

            if (uiState.canUpdateShipment) {
                HorizontalDivider()
                Text(
                    text = "Update Shipment",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // 1. Status Update
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(text = "Current Status", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = it },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = uiState.selectedStatus.replaceFirstChar { it.uppercase() },
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Select Status") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                    shape = RoundedCornerShape(12.dp)
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
                        }

                        // 2. Tracking Number
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(text = "Logistics", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                            OutlinedTextField(
                                value = uiState.trackingNumberInput ?: "",
                                onValueChange = { onEvent(ShipmentDetailEvent.OnTrackingNumberChanged(it)) },
                                label = { Text("Tracking Number") },
                                placeholder = { Text("Enter airway bill number") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // 3. Arrival Proof
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(text = "Proof of Delivery", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                            OutlinedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                onClick = { imagePickerLauncher.launch("image/*") },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.outlinedCardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
                                )
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
                                        else -> {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.CloudUpload,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(42.dp),
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                                Text(
                                                    "Upload Arrival Proof",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // 4. Submit Button
                        val isProcessing = uiState.isUpdating || uiState.isUploadingProof
                        Button(
                            onClick = { onEvent(ShipmentDetailEvent.SubmitAllUpdates) },
                            enabled = !isProcessing,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (isProcessing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Text(
                                    text = "Save Changes",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            } else {
                if (!shipment.arrivalProofUrl.isNullOrBlank()) {
                    HorizontalDivider()
                    Text(
                        text = "Arrival Proof",
                        style = MaterialTheme.typography.titleMedium
                    )
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        CustomAsyncImage(
                            model = shipment.arrivalProofUrl,
                            contentDescription = "Arrival Proof",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
