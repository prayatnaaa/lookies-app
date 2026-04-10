package com.prayatna.lookiesapp.presentation.shipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.merchant.GetShipmentsByMerchantIdUseCase
import com.prayatna.lookiesapp.presentation.shipment.state.ShipmentListUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShipmentListViewModel @Inject constructor(
    private val getShipmentsByMerchantIdUseCase: GetShipmentsByMerchantIdUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(ShipmentListUiState())
    val uiState: StateFlow<ShipmentListUiState> = _uiState.asStateFlow()

    fun getShipmentsByMerchantId(merchantId: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            when (val result = getShipmentsByMerchantIdUseCase(merchantId)) {
                is DataResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = result.error,
                        isLoading = false,
                        data = emptyList()
                    )
                }
                is DataResult.Success-> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = null,
                        isLoading = false,
                        data = result.data
                    )
                }
                else -> Unit
            }
        }
    }
}