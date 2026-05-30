package com.prayatna.lookiesapp.presentation.offlineCheckout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.order.OrderItemInput
import com.prayatna.lookiesapp.domain.usecase.painting.GetEventPaintingByIdUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.CreateOfflineOrderUseCase
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutItemDisplay
import com.prayatna.lookiesapp.presentation.checkout.state.PaymentMethodUiState
import com.prayatna.lookiesapp.presentation.offlineCheckout.state.OfflineCheckoutEffect
import com.prayatna.lookiesapp.presentation.offlineCheckout.state.OfflineCheckoutEvent
import com.prayatna.lookiesapp.presentation.offlineCheckout.state.OfflineCheckoutUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfflineCheckoutViewModel @Inject constructor(
    private val getEventPaintingByIdUseCase: GetEventPaintingByIdUseCase,
    private val createOfflineOrderUseCase: CreateOfflineOrderUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(OfflineCheckoutUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<OfflineCheckoutEffect>()
    val effect = _effect.asSharedFlow()

    init {
        val itemId: String = savedStateHandle["itemId"] ?: ""
        val quantity: Int = savedStateHandle["quantity"] ?: 1
        onEvent(OfflineCheckoutEvent.OnLoad(itemId, quantity))
    }

    fun onEvent(event: OfflineCheckoutEvent) {
        when (event) {
            is OfflineCheckoutEvent.OnLoad -> {
                _uiState.update { it.copy(itemId = event.itemId, quantity = event.quantity) }
                loadData()
            }
            OfflineCheckoutEvent.OnPayClick -> handlePayClick()
            OfflineCheckoutEvent.OnBackClick -> {
                viewModelScope.launch { _effect.emit(OfflineCheckoutEffect.NavigateBack) }
            }
            OfflineCheckoutEvent.OnRefresh -> loadData()
            OfflineCheckoutEvent.DismissError -> _uiState.update { it.copy(errorMessage = null) }
            is OfflineCheckoutEvent.OnPaymentMethodSelected -> {
                _uiState.update { it.copy(selectedMethod = event.method) }
            }
            is OfflineCheckoutEvent.OnBankCodeSelected -> {
                _uiState.update { it.copy(selectedBankCode = event.bankCode) }
            }
        }
    }

    private fun loadData() {
        val itemId = _uiState.value.itemId
        if (itemId.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getEventPaintingByIdUseCase(itemId)) {
                is DataResult.Success -> {
                    val data = result.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            itemToBuy = CheckoutItemDisplay(
                                id = data.id,
                                title = data.painting.title,
                                subtitle = "by ${data.participant.artist.fullName}",
                                price = data.finalPrice,
                                imageUrl = data.painting.paintingUrl,
                                type = "painting",
                                merchantId = data.participant.event.organizer.id
                            )
                        )
                    }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun handlePayClick() {
        val state = _uiState.value
        val item = state.itemToBuy ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val orderItems = listOf(
                OrderItemInput(
                    itemType = "painting",
                    itemRefId = item.id,
                    quantity = state.quantity
                )
            )

            when (val result = createOfflineOrderUseCase(items = orderItems)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true, createdOrderId = result.data) }
                    handlePaymentNavigation(result.data)
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun handlePaymentNavigation(orderId: String) {
        val state = _uiState.value
        val item = state.itemToBuy ?: return
        val total = (item.price ?: 0.0) * state.quantity

        viewModelScope.launch {
            when (state.selectedMethod) {
                PaymentMethodUiState.VA -> {
                    _effect.emit(
                        OfflineCheckoutEffect.NavigateToVaPayment(
                            orderId = orderId,
                            merchantId = item.merchantId,
                            amount = total.toLong(),
                            bankCode = state.selectedBankCode,
                            customerName = "Walk-in Customer"
                        )
                    )
                }

                else -> {
                    _effect.emit(
                        OfflineCheckoutEffect.NavigateToQrisPayment(
                            orderId = orderId,
                            merchantId = item.merchantId,
                            amount = total.toLong()
                        )
                    )
                }
            }
        }
    }
}
