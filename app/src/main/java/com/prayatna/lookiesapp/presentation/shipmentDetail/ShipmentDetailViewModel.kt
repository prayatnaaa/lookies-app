package com.prayatna.lookiesapp.presentation.shipmentDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.domain.usecase.merchant.CreateTrackingNumberShipmentUseCase
import com.prayatna.lookiesapp.domain.usecase.merchant.UpdateShipmentStatusUseCase
import com.prayatna.lookiesapp.presentation.shipmentDetail.state.ShipmentDetailEvent
import com.prayatna.lookiesapp.presentation.shipmentDetail.state.ShipmentDetailUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShipmentDetailViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val updateShipmentStatusUseCase: UpdateShipmentStatusUseCase,
    private val createTrackingNumberShipmentUseCase: CreateTrackingNumberShipmentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShipmentDetailUiState())
    val uiState: StateFlow<ShipmentDetailUiState> = _uiState.asStateFlow()

    fun getShipment(orderId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = transactionRepository.getShipmentByOrderId(orderId)) {
                is DataResult.Success -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            shipment = result.data,
                            selectedStatus = result.data.status,
                            trackingNumberInput = result.data.trackingNumber
                        ) 
                    }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> Unit
            }
        }
    }

    fun onEvent(event: ShipmentDetailEvent) {
        when (event) {
            is ShipmentDetailEvent.OnStatusSelected -> {
                _uiState.update { it.copy(selectedStatus = event.status) }
            }
            is ShipmentDetailEvent.OnTrackingNumberChanged -> {
                _uiState.update { it.copy(trackingNumberInput = event.trackingNumber) }
            }
            ShipmentDetailEvent.SubmitStatusUpdate -> {
                updateStatus()
            }
            ShipmentDetailEvent.SubmitTrackingNumberUpdate -> {
                updateTrackingNumber()
            }
            ShipmentDetailEvent.OnErrorConfirmed -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
            ShipmentDetailEvent.OnSuccessConfirmed -> {
                _uiState.update { it.copy(updateSuccessMessage = null) }
            }
            ShipmentDetailEvent.OnBackClick -> {}
        }
    }

    private fun updateStatus() {
        val shipmentId = _uiState.value.shipment?.id ?: return
        val status = _uiState.value.selectedStatus
        
        if (status.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Status cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isUpdating = true, errorMessage = null) }
            
            when (val result = updateShipmentStatusUseCase(shipmentId = shipmentId, status = status)) {
                is DataResult.Success -> {
                    _uiState.update { 
                        it.copy(
                            isUpdating = false,
                            updateSuccessMessage = "Shipment status updated successfully",
                            shipment = result.data
                        )
                    }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isUpdating = false, errorMessage = result.error) }
                }
                else -> Unit
            }
        }
    }

    private fun updateTrackingNumber() {
        val shipmentId = _uiState.value.shipment?.id ?: return
        val trackingNumber = _uiState.value.trackingNumberInput
        
        if (trackingNumber.isNullOrEmpty()) {
            _uiState.update { it.copy(errorMessage = "Tracking number cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isUpdating = true, errorMessage = null) }
            
            when (val result = createTrackingNumberShipmentUseCase(shipmentId = shipmentId, trackingNumber = trackingNumber)) {
                is DataResult.Success -> {
                    _uiState.update { 
                        it.copy(
                            isUpdating = false,
                            updateSuccessMessage = "Tracking number updated successfully",
                            shipment = result.data
                        )
                    }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isUpdating = false, errorMessage = result.error) }
                }
                else -> Unit
            }
        }
    }
}
