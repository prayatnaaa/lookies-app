package com.prayatna.lookiesapp.presentation.shipmentDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.shipment.ShipmentDetailContent
import com.prayatna.lookiesapp.presentation.shipmentDetail.state.ShipmentDetailEvent
import com.prayatna.lookiesapp.presentation.shipmentDetail.state.ShipmentDetailUiState

@Composable
fun ShipmentDetailScreen(
    uiState: ShipmentDetailUiState,
    onEvent: (ShipmentDetailEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(uiState.updateSuccessMessage) {
        uiState.updateSuccessMessage?.let { message ->
            snackbarHostState.showSnackbar(message, withDismissAction = true)
        }
    }

    if (uiState.errorMessage != null && !uiState.isLoading && uiState.shipment != null) {
        // Show error bottom sheet if it's an action error (not a load error)
        CustomBottomSheet(
            title = "Error",
            message = uiState.errorMessage,
            confirmText = "OK",
            onConfirm = { onEvent(ShipmentDetailEvent.OnErrorConfirmed) },
            onDismiss = { onEvent(ShipmentDetailEvent.OnErrorConfirmed) }
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            BackTopBar(
                title = "Shipment Detail",
                onBackClick = { onEvent(ShipmentDetailEvent.OnBackClick) }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    CircularLoading()
                }
                uiState.shipment != null -> {
                    ShipmentDetailContent(
                        uiState = uiState,
                        onEvent = onEvent,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> {
                    Text(
                        text = uiState.errorMessage ?: "Shipment not found",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }
        }
    }
}
