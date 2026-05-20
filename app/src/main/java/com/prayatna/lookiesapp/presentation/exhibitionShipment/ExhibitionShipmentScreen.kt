package com.prayatna.lookiesapp.presentation.exhibitionShipment

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.shipment.DeliveryMethod
import com.prayatna.lookiesapp.presentation.components.CustomAsyncImage
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.exhibitionShipment.state.ExhibitionShipmentEvent
import com.prayatna.lookiesapp.presentation.exhibitionShipment.state.ExhibitionShipmentUiState

@Composable
fun ExhibitionShipmentScreen(
    uiState: ExhibitionShipmentUiState,
    onEvent: (ExhibitionShipmentEvent) -> Unit,
    backAction: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Error Bottom Sheet
    uiState.errorMessage?.let { message ->
        CustomBottomSheet(
            title = "Error",
            message = message,
            confirmText = "OK",
            onConfirm = {
                onEvent(ExhibitionShipmentEvent.DismissError)
                backAction()
            },
            onDismiss = {
                onEvent(ExhibitionShipmentEvent.DismissError)
                backAction()
            }
        )
    }

    // Success Snackbar
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(ExhibitionShipmentEvent.DismissSuccess)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            BackTopBar(
                onBackClick = { onEvent(ExhibitionShipmentEvent.OnBackClick) },
                title = "Exhibition Shipment"
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // ── Always-visible context header ─────────────────────────────
            ExhibitionContextCard(uiState = uiState)

            HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))

            // ── Phase-specific action section ─────────────────────────────
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (uiState.eventPaintingStatus) {

                    "approved", "accepted" -> InboundSubmitSection(uiState, onEvent)

                    "shipping_to_event" -> OrganizerConfirmReceivedSection(uiState, onEvent)

                    "exhibited" -> StatusInfoSection(
                        title = "Artwork is Being Exhibited",
                        body = "Your artwork is currently on display at the gallery. No action is needed right now.",
                        isPositive = true
                    )

                    "unsold" -> ReturnShipmentSection(uiState, onEvent)

                    "returning_to_creator" -> ArtistConfirmReturnSection(uiState, onEvent)

                    "returned" -> StatusInfoSection(
                        title = "Artwork Returned",
                        body = "The artwork has been successfully returned to you. The exhibition cycle is complete.",
                        isPositive = true
                    )

                    else -> StatusInfoSection(
                        title = "Status: ${uiState.eventPaintingStatus}",
                        body = "No shipment action is required at this stage.",
                        isPositive = false
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

// ── Context header card ───────────────────────────────────────────────────────
@Composable
private fun ExhibitionContextCard(uiState: ExhibitionShipmentUiState) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            if (uiState.paintingTitle.isNotBlank()) {
                Text(
                    text = uiState.paintingTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(12.dp))
            }

            if (uiState.eventTitle.isNotBlank()) {
                ContextRow(
                    icon = Icons.Default.Storefront,
                    label = "Exhibition",
                    value = buildString {
                        append(uiState.eventTitle)
                        if (uiState.organizerName.isNotBlank()) append("\nby ${uiState.organizerName}")
                    }
                )
                Spacer(Modifier.height(10.dp))
            }

            if (uiState.eventLocation.isNotBlank()) {
                ContextRow(
                    icon = Icons.Default.LocationOn,
                    label = "Drop-off Location",
                    value = uiState.eventLocation
                )
                Spacer(Modifier.height(10.dp))
            }

            if (uiState.eventStartDate.isNotBlank() || uiState.eventEndDate.isNotBlank()) {
                val dateRange = when {
                    uiState.eventStartDate.isNotBlank() && uiState.eventEndDate.isNotBlank() ->
                        "${uiState.eventStartDate}  →  ${uiState.eventEndDate}"
                    uiState.eventEndDate.isNotBlank() -> "Until ${uiState.eventEndDate}"
                    else -> uiState.eventStartDate
                }
                ContextRow(
                    icon = Icons.Default.CalendarMonth,
                    label = "Event Period",
                    value = dateRange
                )
            }
        }
    }
}

@Composable
private fun ContextRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(18.dp)
                .padding(top = 1.dp)
        )
        Spacer(Modifier.width(10.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

// ── Inbound: Artist submits shipment ─────────────────────────────────────────
@Composable
private fun InboundSubmitSection(
    uiState: ExhibitionShipmentUiState,
    onEvent: (ExhibitionShipmentEvent) -> Unit
) {
    Text(
        text = "Submit Artwork for Exhibition",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = "Your artwork has been approved. Choose how you'll deliver it and provide shipment details.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    DeliveryMethodSelector(
        selected = uiState.selectedDeliveryMethod,
        onSelect = { onEvent(ExhibitionShipmentEvent.OnDeliveryMethodSelected(it)) }
    )

    if (uiState.selectedDeliveryMethod == DeliveryMethod.COURIER) {
        OutlinedTextField(
            value = uiState.courierNameInput,
            onValueChange = { onEvent(ExhibitionShipmentEvent.OnCourierNameChanged(it)) },
            label = { Text("Courier Name") },
            placeholder = { Text("e.g. JNE, SiCepat, Anteraja") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.trackingNumberInput,
            onValueChange = { onEvent(ExhibitionShipmentEvent.OnTrackingNumberChanged(it)) },
            label = { Text("Tracking Number") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }

    Button(
        onClick = { onEvent(ExhibitionShipmentEvent.SubmitInboundShipment) },
        enabled = !uiState.isSubmitting,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        if (uiState.isSubmitting) CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            strokeWidth = 2.dp
        )
        else Text("Submit Shipment", fontWeight = FontWeight.Bold)
    }
}

// ── Inbound: Organizer confirms artwork received ──────────────────────────────
@Composable
private fun OrganizerConfirmReceivedSection(
    uiState: ExhibitionShipmentUiState,
    onEvent: (ExhibitionShipmentEvent) -> Unit
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            onEvent(ExhibitionShipmentEvent.OnArrivalProofSelected(it))
        }
    }

    Text(
        text = "Confirm Artwork Received",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = "Verify that the artwork has physically arrived at the gallery and is in acceptable condition.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Text(text = "Arrival Proof", style = MaterialTheme.typography.labelLarge)
    if (uiState.shipment?.arrivalProofUrl != null) {
        CustomAsyncImage(
            model = uiState.shipment.arrivalProofUrl,
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
                onClick = { onEvent(ExhibitionShipmentEvent.SubmitArrivalProof) },
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

    OutlinedTextField(
        value = uiState.notesInput,
        onValueChange = { onEvent(ExhibitionShipmentEvent.OnGalleryNotesChanged(it)) },
        label = { Text("Condition Notes (optional)") },
        placeholder = { Text("Describe the artwork's condition on arrival") },
        minLines = 3,
        modifier = Modifier.fillMaxWidth()
    )

    Button(
        onClick = { onEvent(ExhibitionShipmentEvent.ConfirmArtworkReceived) },
        enabled = !uiState.isSubmitting,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        if (uiState.isSubmitting) CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            strokeWidth = 2.dp
        )
        else Text("Confirm Artwork Received", fontWeight = FontWeight.Bold)
    }
}

// ── Outbound: Organizer arranges return ───────────────────────────────────────
@Composable
private fun ReturnShipmentSection(
    uiState: ExhibitionShipmentUiState,
    onEvent: (ExhibitionShipmentEvent) -> Unit
) {
    Text(
        text = "Arrange Return Shipment",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = "The exhibition has ended and this artwork is unsold. Enter the return courier details to ship it back to the creator.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    OutlinedTextField(
        value = uiState.courierNameInput,
        onValueChange = { onEvent(ExhibitionShipmentEvent.OnCourierNameChanged(it)) },
        label = { Text("Courier Name") },
        placeholder = { Text("e.g. JNE, SiCepat") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = uiState.returnTrackingNumberInput,
        onValueChange = { onEvent(ExhibitionShipmentEvent.OnReturnTrackingNumberChanged(it)) },
        label = { Text("Return Tracking Number") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )

    Button(
        onClick = { onEvent(ExhibitionShipmentEvent.SubmitReturnShipment) },
        enabled = !uiState.isSubmitting,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        if (uiState.isSubmitting) CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            strokeWidth = 2.dp
        )
        else Text("Submit Return Shipment", fontWeight = FontWeight.Bold)
    }
}

// ── Outbound: Artist confirms artwork received back ───────────────────────────
@Composable
private fun ArtistConfirmReturnSection(
    uiState: ExhibitionShipmentUiState,
    onEvent: (ExhibitionShipmentEvent) -> Unit
) {
    Text(
        text = "Confirm Artwork Received",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = "Confirm that you have physically received your artwork back from the exhibition.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    OutlinedTextField(
        value = uiState.notesInput,
        onValueChange = { onEvent(ExhibitionShipmentEvent.OnArtistConditionNotesChanged(it)) },
        label = { Text("Condition Notes (optional)") },
        placeholder = { Text("Note any damage or issues with the returned artwork") },
        minLines = 3,
        modifier = Modifier.fillMaxWidth()
    )

    Button(
        onClick = { onEvent(ExhibitionShipmentEvent.ConfirmArtworkReturned) },
        enabled = !uiState.isSubmitting,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        if (uiState.isSubmitting) CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            strokeWidth = 2.dp
        )
        else Text("Confirm Receipt", fontWeight = FontWeight.Bold)
    }
}

// ── Delivery method radio selector ───────────────────────────────────────────
@Composable
private fun DeliveryMethodSelector(
    selected: DeliveryMethod,
    onSelect: (DeliveryMethod) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Delivery Method",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(4.dp))
        DeliveryMethod.entries.forEach { method ->
            val label = when (method) {
                DeliveryMethod.SELF_DROP_OFF -> "Self Drop-Off"
                DeliveryMethod.COURIER -> "Courier"
                DeliveryMethod.ARTIST_PICKUP -> "Pick-Up by Organizer"
            }
            val description = when (method) {
                DeliveryMethod.SELF_DROP_OFF -> "You personally deliver to the venue"
                DeliveryMethod.COURIER -> "Ship via a courier with tracking"
                DeliveryMethod.ARTIST_PICKUP -> "Organizer collects from you"
            }
            Surface(
                color = if (selected == method)
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(10.dp),
                tonalElevation = if (selected == method) 0.dp else 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selected == method,
                        onClick = { onSelect(method) },
                        role = Role.RadioButton
                    )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selected == method,
                        onClick = null
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (selected == method) FontWeight.SemiBold else FontWeight.Normal
                        )
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (selected == method) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

// ── Info-only status section ─────────────────────────────────────────────────
@Composable
private fun StatusInfoSection(title: String, body: String, isPositive: Boolean) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isPositive)
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (isPositive) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = body,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
