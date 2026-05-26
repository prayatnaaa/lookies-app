package com.prayatna.lookiesapp.presentation.transaction.detailTransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.data.remote.dto.response.payment.SetOrderToCompleteInput
import com.prayatna.lookiesapp.domain.usecase.transaction.GetDetailPaintingOrderUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.GetDetailTransactionUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.SetOrderToCompleteUseCase
import com.prayatna.lookiesapp.presentation.transaction.detailTransaction.state.DetailTransactionUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailTransactionViewModel @Inject constructor(
    private val getDetailTransactionUseCase: GetDetailTransactionUseCase,
    private val getDetailPaintingOrderUseCase: GetDetailPaintingOrderUseCase,
    private val setOrderToCompleteUseCase: SetOrderToCompleteUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(DetailTransactionUiState())
    val uiState: StateFlow<DetailTransactionUiState> = _uiState.asStateFlow()

    fun getDetailTransaction(orderId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when(val result = getDetailTransactionUseCase(orderId)) {
                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.error,
                            isLoading = false
                        )
                    }
                }
                is DataResult.Success -> {
                    val detailTx = result.data
                    val isPainting = detailTx.transaction.items.any { it.itemType == "painting" }
                    
                    if (isPainting) {
                        when (val paintingResult = getDetailPaintingOrderUseCase(orderId)) {
                            is DataResult.Success -> {
                                _uiState.update {
                                    it.copy(
                                        errorMessage = null,
                                        isLoading = false,
                                        data = detailTx,
                                        shipment = paintingResult.data.shipment
                                    )
                                }
                            }
                            is DataResult.Error -> {
                                _uiState.update {
                                    it.copy(
                                        errorMessage = paintingResult.error,
                                        isLoading = false
                                    )
                                }
                            }
                            else -> Unit
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                errorMessage = null,
                                isLoading = false,
                                data = detailTx,
                                shipment = null
                            )
                        }
                    }
                }
                else -> Unit
            }

        }
    }

    fun setOrderToComplete(orderId: String) {
        val input = SetOrderToCompleteInput(orderId)
        viewModelScope.launch {
            _uiState.update { it.copy(isCompleting = true) }
            when (val result = setOrderToCompleteUseCase(input)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isCompleting = false) }
                    getDetailTransaction(orderId)
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isCompleting = false, errorMessage = result.error) }
                }
                else -> Unit
            }
        }
    }

}