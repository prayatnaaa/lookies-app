package com.prayatna.lookiesapp.presentation.scanner

import com.prayatna.lookiesapp.domain.model.ticket.Ticket

data class ScannerUiState(
    val scannedTicket: Ticket? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

sealed interface ScannerUiEvent {
    data class OnImageProxyAvailable(val imageProxy: androidx.camera.core.ImageProxy) : ScannerUiEvent
    data object OnDismissError : ScannerUiEvent
    data object OnResetScan : ScannerUiEvent
}

sealed interface ScannerUiEffect {
    data class ShowSnackbar(val message: String) : ScannerUiEffect
    data class OnTicketVerified(val message: String) : ScannerUiEffect
    data object NavigateBack : ScannerUiEffect
}
