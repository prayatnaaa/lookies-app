package com.prayatna.lookiesapp.presentation.unsoldArtworkReturn

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.domain.model.user.BusinessAddress
import com.prayatna.lookiesapp.presentation.components.CustomAsyncImage
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.unsoldArtworkReturn.state.UnsoldArtworkReturnEvent
import com.prayatna.lookiesapp.presentation.unsoldArtworkReturn.state.UnsoldArtworkReturnUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnsoldArtworkReturnScreen(
    uiState: UnsoldArtworkReturnUiState,
    onEvent: (UnsoldArtworkReturnEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            onEvent(UnsoldArtworkReturnEvent.DismissSuccess)
        }
    }

    if (uiState.errorMessage != null) {
        CustomBottomSheet(
            title = "Error",
            message = uiState.errorMessage,
            confirmText = "OK",
            onConfirm = { onEvent(UnsoldArtworkReturnEvent.DismissError) },
            onDismiss = { onEvent(UnsoldArtworkReturnEvent.DismissError) }
        )
    }

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = { onEvent(UnsoldArtworkReturnEvent.OnBackClick) },
                title = "Artwork Return"
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading && uiState.eventPainting == null) {
                CircularLoading(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .imePadding()
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Artwork Context Card
                    ArtworkHeaderCard(uiState)

                    // Return Destination (Merchant/Artist Address)
                    if (uiState.returnAddress != null) {
                        ReturnAddressCard(address = uiState.returnAddress)
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    // Actions based on status
                    when (uiState.eventPainting?.status) {
                        "unsold" -> OrganizerReturnForm(uiState, onEvent)
                        "returning_to_creator" -> ArtistConfirmReceipt(uiState, onEvent)
                        "returned" -> SuccessInfoSection(
                            title = "Artwork Returned",
                            body = "The artwork has been successfully returned to the artist. The exhibition process is complete."
                        )
                        else -> {
                           Text(
                               text = "Current Status: ${uiState.eventPainting?.status}",
                               style = MaterialTheme.typography.bodyMedium,
                               modifier = Modifier.padding(8.dp)
                           )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun ArtworkHeaderCard(uiState: UnsoldArtworkReturnUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomAsyncImage(
                model = uiState.eventPainting?.painting?.paintingUrl,
                contentDescription = uiState.paintingTitle,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = uiState.paintingTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = uiState.artistName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "From: ${uiState.eventTitle}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun ReturnAddressCard(address: BusinessAddress) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Return to Original Location",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = listOfNotNull(address.streetLine1, address.streetLine2).joinToString(", "),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = listOfNotNull(address.city, address.provinceState, address.postalCode).joinToString(", "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = address.country,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun OrganizerReturnForm(
    uiState: UnsoldArtworkReturnUiState,
    onEvent: (UnsoldArtworkReturnEvent) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Initiate Return Shipment",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "The artwork is unsold. Please enter the courier details to send it back to the artist's address.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = uiState.courierNameInput,
            onValueChange = { onEvent(UnsoldArtworkReturnEvent.OnCourierNameChanged(it)) },
            label = { Text("Courier Name") },
            placeholder = { Text("e.g. JNE, SiCepat") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = uiState.trackingNumberInput,
            onValueChange = { onEvent(UnsoldArtworkReturnEvent.OnTrackingNumberChanged(it)) },
            label = { Text("Tracking Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = { onEvent(UnsoldArtworkReturnEvent.SubmitReturnShipment) },
            enabled = !uiState.isSubmitting && uiState.trackingNumberInput.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (uiState.isSubmitting) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            else Text("Send Back to Artist", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ArtistConfirmReceipt(
    uiState: UnsoldArtworkReturnUiState,
    onEvent: (UnsoldArtworkReturnEvent) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Confirm Receipt of Artwork",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "Shipping Information", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Text(text = "Courier: ${uiState.shipment?.courierName ?: "Unknown"}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Tracking: ${uiState.shipment?.trackingNumber ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
            }
        }

        OutlinedTextField(
            value = uiState.notesInput,
            onValueChange = { onEvent(UnsoldArtworkReturnEvent.OnNotesChanged(it)) },
            label = { Text("Condition Notes (Optional)") },
            placeholder = { Text("Describe the condition of your artwork upon return") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Button(
            onClick = { onEvent(UnsoldArtworkReturnEvent.ConfirmArtworkReturned) },
            enabled = !uiState.isSubmitting,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            if (uiState.isSubmitting) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            else Text("Confirm Artwork Received", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SuccessInfoSection(title: String, body: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                Spacer(Modifier.width(8.dp))
                Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
            }
            Spacer(Modifier.height(8.dp))
            Text(text = body, style = MaterialTheme.typography.bodySmall)
        }
    }
}
