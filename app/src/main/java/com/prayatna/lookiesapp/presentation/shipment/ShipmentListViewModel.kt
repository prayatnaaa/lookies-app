package com.prayatna.lookiesapp.presentation.shipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.merchant.GetShipmentsByMerchantIdUseCase
import com.prayatna.lookiesapp.presentation.shipment.state.ShipmentListUiEvent
import com.prayatna.lookiesapp.presentation.shipment.state.ShipmentListUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShipmentListViewModel @Inject constructor(
    private val getShipmentsByMerchantIdUseCase: GetShipmentsByMerchantIdUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(ShipmentListUiState())
    val uiState: StateFlow<ShipmentListUiState> = _uiState.asStateFlow()
    
    private var currentMerchantId: String = ""

    fun onEvent(event: ShipmentListUiEvent) {
        when (event) {
            is ShipmentListUiEvent.LoadShipments -> {
                currentMerchantId = event.merchantId
                getShipments()
            }
            is ShipmentListUiEvent.FilterByStatus -> {
                _uiState.update { it.copy(selectedStatus = event.status) }
                getShipments()
            }
            ShipmentListUiEvent.Refresh -> {
                getShipments()
            }
        }
    }

    private fun getShipments() {
        if (currentMerchantId.isEmpty()) return
        
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            when (val result = getShipmentsByMerchantIdUseCase(currentMerchantId, _uiState.value.selectedStatus)) {
                is DataResult.Error -> {
                    _uiState.update { it.copy(
                        errorMessage = result.error,
                        isLoading = false,
                        data = emptyList()
                    ) }
                }
                is DataResult.Success-> {
                    _uiState.update { it.copy(
                        errorMessage = null,
                        isLoading = false,
                        data = result.data
                    ) }
                }
                else -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}
