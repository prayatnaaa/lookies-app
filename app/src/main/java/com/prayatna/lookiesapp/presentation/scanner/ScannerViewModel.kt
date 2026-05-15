package com.prayatna.lookiesapp.presentation.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.admin.VerifyUserTicketUseCase
import com.prayatna.lookiesapp.domain.usecase.scanner.ScanBarcodeUseCase
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val scanBarcodeUseCase: ScanBarcodeUseCase,
    private val verifyUserTicketUseCase: VerifyUserTicketUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ScannerUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    fun onEvent(event: ScannerUiEvent) {
        when (event) {
            is ScannerUiEvent.OnImageProxyAvailable -> {
                if (!_uiState.value.isSuccess && !_uiState.value.isLoading) {
                    viewModelScope.launch {
                        try {
                            val barcodes = scanBarcodeUseCase(event.imageProxy)
                                .catch { e ->
                                    _uiState.update { it.copy(errorMessage = e.message) }
                                    event.imageProxy.close()
                                }
                                .first()

                            if (barcodes.isNotEmpty()) {
                                val ticketCode = barcodes.firstNotNullOfOrNull { it.rawValue }
                                if (ticketCode != null) {
                                    verifyTicket(ticketCode)
                                }
                            }
                        } catch (e: Exception) {
                            _uiState.update { it.copy(errorMessage = e.message) }
                        }
                    }
                } else {
                    event.imageProxy.close()
                }
            }
            ScannerUiEvent.OnDismissError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
            ScannerUiEvent.OnResetScan -> {
                _uiState.update { it.copy(
                    scannedTicket = null,
                    isSuccess = false,
                    errorMessage = null,
                    isLoading = false
                ) }
            }
        }
    }

    private fun verifyTicket(code: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = verifyUserTicketUseCase(code)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(
                        scannedTicket = result.data,
                        isSuccess = true,
                        isLoading = false
                    ) }
                    _uiEffect.emit(ScannerUiEffect.OnTicketVerified(result.data))
                    _uiEffect.emit(ScannerUiEffect.ShowToast("Ticket Verified: ${result.data.ticketCode}"))
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(
                        errorMessage = result.error,
                        isLoading = false
                    ) }
                    _uiEffect.emit(ScannerUiEffect.ShowToast("Error: ${result.error}"))
                }
                else -> {
                    // Handle Idle or Loading if necessary
                }
            }
        }
    }
}
