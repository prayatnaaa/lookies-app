package com.prayatna.lookiesapp.presentation.components.shipment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                    modifier = Modifier.fillMaxWidth(),
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
