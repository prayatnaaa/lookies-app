package com.prayatna.lookiesapp.presentation.admin.transactionDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.admin.GetAdminTransactionDetailUseCase
import com.prayatna.lookiesapp.presentation.admin.transactionDetail.state.AdminTransactionDetailUiEffect
import com.prayatna.lookiesapp.presentation.admin.transactionDetail.state.AdminTransactionDetailUiEvent
import com.prayatna.lookiesapp.presentation.admin.transactionDetail.state.AdminTransactionDetailUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminTransactionDetailViewModel @Inject constructor(
    private val getAdminTransactionDetailUseCase: GetAdminTransactionDetailUseCase,
//    private val getDetailTransactionUseCase: GetDetailTransactionUseCase,
//    private val getDetailPaintingOrderUseCase: GetDetailPaintingOrderUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderId: String = savedStateHandle["orderId"] ?: ""

    private val _uiState = MutableStateFlow(AdminTransactionDetailUiState())
    val uiState: StateFlow<AdminTransactionDetailUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<AdminTransactionDetailUiEffect>()
    val effect: SharedFlow<AdminTransactionDetailUiEffect> = _effect.asSharedFlow()

    init {
        if (orderId.isNotEmpty()) {
            loadDetail(orderId)
        }
    }

    fun onEvent(event: AdminTransactionDetailUiEvent) {
        when (event) {
            is AdminTransactionDetailUiEvent.LoadTransactionDetail -> loadDetail(event.orderId)
            AdminTransactionDetailUiEvent.OnBackClicked -> {
                viewModelScope.launch {
                    _effect.emit(AdminTransactionDetailUiEffect.NavigateBack)
                }
            }
            AdminTransactionDetailUiEvent.Refresh -> loadDetail(orderId)
        }
    }

    private fun loadDetail(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val adminDetailDef = async { getAdminTransactionDetailUseCase(id) }
//            val detailTxDef = async { getDetailTransactionUseCase(id) }
//            val paintingOrderDef = async { getDetailPaintingOrderUseCase(id) }

            //            val detailTx = detailTxDef.await()
//            val paintingOrder = paintingOrderDef.await()

            when (val adminDetail = adminDetailDef.await()) {
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            transaction = adminDetail.data,
        //                    detailTransaction = if (detailTx is DataResult.Success) detailTx.data else null,
        //                    shipment = if (paintingOrder is DataResult.Success) paintingOrder.data.shipment else null
                        )
                    }
                }

                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = adminDetail.error) }
                    _effect.emit(AdminTransactionDetailUiEffect.ShowError(adminDetail.error))
                }

                else -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}
