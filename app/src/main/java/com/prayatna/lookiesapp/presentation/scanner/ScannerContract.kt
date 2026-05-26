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
    data class ShowToast(val message: String) : ScannerUiEffect
    data class OnTicketVerified(val ticket: Ticket) : ScannerUiEffect
    data object NavigateBack : ScannerUiEffect
}
