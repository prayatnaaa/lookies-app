package com.prayatna.lookiesapp.presentation.scanner

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.scanner.ScanBarcodeUseCase
import com.prayatna.lookiesapp.domain.usecase.ticket.VerifyAndConsumeTicketUseCase
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
    private val verifyAndConsumeTicketUseCase: VerifyAndConsumeTicketUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val eventId: Int = checkNotNull(savedStateHandle["eventId"])

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
                                    verifyTicket(ticketCode, eventId = eventId.toInt())
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

    private fun verifyTicket(code: String, eventId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            when (val result = verifyAndConsumeTicketUseCase(code, eventId)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(
                        isSuccess = true,
                        isLoading = false
                    ) }
                    _uiEffect.emit(ScannerUiEffect.OnTicketVerified("Ticket successfully scanned and consumed"))
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(
                        errorMessage = result.error,
                        isLoading = false
                    ) }
                    _uiEffect.emit(ScannerUiEffect.ShowSnackbar("Error: ${result.error}"))
                }
                else -> Unit
            }
        }
    }
}
