package com.prayatna.lookiesapp.presentation.components.shipment

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.components.CustomAsyncImage
import com.prayatna.lookiesapp.presentation.shipmentDetail.state.ShipmentDetailEvent
import com.prayatna.lookiesapp.presentation.shipmentDetail.state.ShipmentDetailUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShipmentDetailContent(
    uiState: ShipmentDetailUiState,
    onEvent: (ShipmentDetailEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val shipment = uiState.shipment
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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (shipment != null) {
            Text(text = "Order ID", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            Text(text = shipment.orderId, style = MaterialTheme.typography.bodyLarge)

            HorizontalDivider()

            Text(text = "Recipient Info", style = MaterialTheme.typography.titleMedium)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Name: ${shipment.reciepentName}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Phone: ${shipment.phoneNumber}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Address: ${shipment.addressLine}\n${shipment.province}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            HorizontalDivider()

            Text(text = "Arrival Proof", style = MaterialTheme.typography.titleMedium)
            
            if (shipment.arrivalProofUrl != null) {
                CustomAsyncImage(
                    model = shipment.arrivalProofUrl,
                    contentDescription = "Arrival Proof",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            } else {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    onClick = { imagePickerLauncher.launch("image/*") }
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (uiState.selectedArrivalProof != null) {
                            CustomAsyncImage(
                                model = uiState.selectedArrivalProof,
                                contentDescription = "Selected Proof",
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(48.dp))
                                Text("Upload Arrival Proof", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }

                if (uiState.selectedArrivalProof != null) {
                    Button(
                        onClick = { onEvent(ShipmentDetailEvent.SubmitArrivalProof) },
                        enabled = !uiState.isUploadingProof,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (uiState.isUploadingProof) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Text("Submit Proof")
                        }
                    }
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
