package com.prayatna.lookiesapp.presentation.checkout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.order.OrderItemInput
import com.prayatna.lookiesapp.domain.usecase.event.GetEventByIdUseCase
import com.prayatna.lookiesapp.domain.usecase.painting.GetEventPaintingByIdUseCase
import com.prayatna.lookiesapp.domain.usecase.shipment.GetShipmentFeeUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.CreateOrderUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.GetDetailTransactionUseCase
import com.prayatna.lookiesapp.domain.usecase.user.GetUserAddressesUseCase
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutEffect
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutEvent
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutItemDisplay
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutUiState
import com.prayatna.lookiesapp.presentation.checkout.state.PaymentMethodUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val createOrderUseCase: CreateOrderUseCase,
    private val getEventByIdUseCase: GetEventByIdUseCase,
    private val getEventPaintingByIdUseCase: GetEventPaintingByIdUseCase,
    private val getDetailTransactionUseCase: GetDetailTransactionUseCase,
    private val getShipmentFeeUseCase: GetShipmentFeeUseCase,
    private val getUserAddressesUseCase: GetUserAddressesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CheckoutEffect>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val effect = _effect.asSharedFlow()

    fun refreshUserAddresses() {
        viewModelScope.launch {
            fetchUserAddresses()
        }
    }
    // ========================
    // SINGLE ENTRY POINT
    // ========================
    fun onEvent(event: CheckoutEvent) {
        when (event) {

            is CheckoutEvent.OnLoad -> {
                _uiState.update {
                    it.copy(
                        type = event.type,
                        itemId = event.itemId,
                        quantity = event.quantity
                    )
                }
                loadData()
            }

            CheckoutEvent.OnRefresh -> loadData()

            CheckoutEvent.OnBackClick -> {
                emitEffect(CheckoutEffect.NavigateBack)
            }

            CheckoutEvent.OnAddAddressClick -> {
                emitEffect(CheckoutEffect.NavigateToAddAddress)
            }

            CheckoutEvent.OnPayClick -> handlePayClick()

            is CheckoutEvent.OnPaymentMethodSelected -> {
                _uiState.update { it.copy(selectedMethod = event.method) }
            }

            is CheckoutEvent.OnBankCodeSelected -> {
                _uiState.update { it.copy(selectedBankCode = event.bankCode) }
            }

            is CheckoutEvent.OnShipmentFeeSelected -> {
                _uiState.update { it.copy(selectedShipmentFee = event.fee) }
            }

            is CheckoutEvent.OnAddressSelected -> {
                _uiState.update { it.copy(selectedAddress = event.address) }
            }
        }
    }

    // ========================
    // LOAD DATA
    // ========================
    private fun loadData() {
        val state = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (state.type) {
                "event_ticket" -> handleEventFetch(state.itemId)

                "painting" -> {
                    handlePaintingFetch(state.itemId)
                    fetchShipmentFees()
                    fetchUserAddresses()
                }

                "event_registration" -> {
                    handleEventRegistrationFetch(state.itemId)
                }

                else -> emitEffect(CheckoutEffect.ShowErrorDialog("Invalid type"))
            }
        }
    }

    // ========================
    // PAY LOGIC
    // ========================
    private fun handlePayClick() {
        val state = _uiState.value
        val item = state.itemToBuy ?: run {
            emitEffect(CheckoutEffect.ShowErrorDialog("Item not found"))
            return
        }

        if (state.type == "event_registration") {
            val orderId = state.existingOrderId ?: return
            navigateToPayment(orderId)
            return
        }

        val orderItem = OrderItemInput(
            itemType = item.type,
            itemRefId = item.id,
            quantity = state.quantity
        )

        createCheckout( items = listOf(orderItem))
    }

    // ========================
    // CREATE ORDER
    // ========================
    private fun createCheckout(items: List<OrderItemInput>) {
        viewModelScope.launch {
            val state = _uiState.value
            val item = state.itemToBuy ?: return@launch

            val shippingCost =
                if (item.type == "painting")
                    state.selectedShipmentFee?.fee?.toDouble() ?: 0.0
                else 0.0

            if (item.type == "painting" && state.selectedAddress == null) {
                emitEffect(CheckoutEffect.ShowSnackbar("Select address"))
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }

            when (val result = createOrderUseCase(
                items = items,
                shippingCost = shippingCost,
                recipientName = state.selectedAddress?.name ?: "",
                phoneNumber = state.selectedAddress?.phoneNumber ?: "",
                addressLine = state.selectedAddress?.addressLine ?: "",
                province = state.selectedAddress?.province ?: "",
                postalCode = state.selectedAddress?.postalCode ?: "",
            )) {

                is DataResult.Success -> {
                    Log.d("CHECKOUT", "type=${item.type}")
                    Log.d("CHECKOUT", "id=${item.id}")
                    _uiState.update { it.copy(isLoading = false) }
                    handleCheckoutSuccess(
                        orderId = result.data,
                        type = item.type,
                        quantity = state.quantity
                    )
                }

                is DataResult.Error -> {
                    Log.d("CHECKOUT", "type=${item.type}")
                    Log.d("CHECKOUT", "id=${item.id}")

                    _uiState.update { it.copy(isLoading = false) }
                    emitEffect(CheckoutEffect.ShowErrorDialog(result.error))
                }

                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // ========================
    // NAVIGATION
    // ========================
    private fun navigateToPayment(orderId: String) {
        val state = _uiState.value
        val item = state.itemToBuy ?: return

        val shippingCost =
            if (state.type == "painting")
                state.selectedShipmentFee?.fee?.toDouble() ?: 0.0
            else 0.0

        val total =
            (item.price?.times(state.quantity)?.plus(shippingCost))?.toLong() ?: 0L

        when (state.selectedMethod) {
            PaymentMethodUiState.VA -> {
                emitEffect(
                    CheckoutEffect.NavigateToVaPayment(
                        orderId = orderId,
                        merchantId = item.merchantId,
                        amount = total,
                        bankCode = state.selectedBankCode,
                        customerName = state.selectedAddress?.name ?: "Customer"
                    )
                )
            }
            else -> {
                emitEffect(
                    CheckoutEffect.NavigateToQrisPayment(
                        orderId = orderId,
                        merchantId = item.merchantId,
                        amount = total
                    )
                )
            }
        }
    }

    // ========================
    // NETWORK HANDLERS
    // ========================
    private suspend fun handleEventFetch(id: String) {
        when (val result = getEventByIdUseCase(id)) {
            is DataResult.Success -> {
                val event = result.data
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        itemToBuy = CheckoutItemDisplay(
                            id = event.id,
                            title = event.title,
                            subtitle = "by ${event.organizer.legalName}",
                            price = event.ticketPrice,
                            imageUrl = event.bannerImageUrl,
                            type = "event_ticket",
                            merchantId = event.organizer.id
                        )
                    )
                }
            }

            is DataResult.Error -> {
                _uiState.update { it.copy(isLoading = false) }
                emitEffect(CheckoutEffect.ShowErrorDialog(result.error))
            }

            else -> Unit
        }
    }

    private suspend fun handlePaintingFetch(id: String) {
        when (val result = getEventPaintingByIdUseCase(id)) {
            is DataResult.Success -> {
                val data = result.data
                Log.d("CHECKOUT", "handlePaintingFetch: $data")
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
                _uiState.update { it.copy(isLoading = false) }
                emitEffect(CheckoutEffect.ShowErrorDialog(result.error))
            }

            else -> Unit
        }
    }

    private suspend fun handleEventRegistrationFetch(orderId: String) {
        when (val result = getDetailTransactionUseCase(orderId)) {
            is DataResult.Success -> {
                val transaction = result.data.transaction
                val firstItem = transaction.items.firstOrNull()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        existingOrderId = orderId,
                        itemToBuy = CheckoutItemDisplay(
                            id = transaction.id,
                            title = firstItem?.details?.title ?: "Event Registration",
                            subtitle = "Registration Fee",
                            price = transaction.totalAmount,
                            imageUrl = firstItem?.details?.imageUrl ?: "",
                            type = "event_registration",
                            merchantId = transaction.merchantId
                        )
                    )
                }
            }

            is DataResult.Error -> {
                Log.e("CheckoutViewModel", "handleEventRegistrationFetch: ${result.error}")
                _uiState.update { it.copy(isLoading = false) }
                emitEffect(CheckoutEffect.ShowErrorDialog(result.error))
            }

            else -> Unit
        }
    }

    private suspend fun fetchShipmentFees() {
        _uiState.update { it.copy(isShipmentLoading = true) }

        when (val result = getShipmentFeeUseCase()) {
            is DataResult.Success -> {
                val fees = result.data
                _uiState.update {
                    it.copy(
                        isShipmentLoading = false,
                        shipmentFees = fees,
                        selectedShipmentFee = fees.firstOrNull()
                    )
                }
            }

            is DataResult.Error -> {
                _uiState.update { it.copy(isShipmentLoading = false) }
                emitEffect(CheckoutEffect.ShowErrorDialog(result.error))
            }

            else -> Unit
        }
    }

    private suspend fun fetchUserAddresses() {
        _uiState.update { it.copy(isAddressLoading = true) }

        when (val result = getUserAddressesUseCase()) {
            is DataResult.Success -> {
                val addresses = result.data
                _uiState.update { checkoutUiState ->
                    checkoutUiState.copy(
                        isAddressLoading = false,
                        userAddresses = addresses,
                        selectedAddress = addresses.firstOrNull { it.isDefault }
                            ?: addresses.firstOrNull()
                    )
                }
            }

            is DataResult.Error -> {
                _uiState.update { it.copy(isAddressLoading = false) }
                emitEffect(CheckoutEffect.ShowErrorDialog(result.error))
            }

            else -> Unit
        }
    }

    private fun handleCheckoutSuccess(orderId: String, type: String, quantity: Int) {
        viewModelScope.launch {
            val state = _uiState.value
            val item = state.itemToBuy ?: return@launch

            val shippingCost = if (type == "painting") {
                state.selectedShipmentFee?.fee?.toDouble() ?: 0.0
            } else 0.0

            val totalAmount =
                (item.price?.times(quantity)?.plus(shippingCost))?.toLong() ?: 0L

            when (state.selectedMethod) {
                PaymentMethodUiState.VA -> {
                    emitEffect(
                        CheckoutEffect.NavigateToVaPayment(
                            orderId = orderId,
                            merchantId = item.merchantId,
                            amount = totalAmount,
                            bankCode = state.selectedBankCode,
                            customerName = state.selectedAddress?.name ?: "Customer"
                        )
                    )
                }
                else -> {
                    emitEffect(
                        CheckoutEffect.NavigateToQrisPayment(
                            orderId = orderId,
                            merchantId = item.merchantId,
                            amount = totalAmount
                        )
                    )
                }
            }
        }
    }

    private fun emitEffect(effect: CheckoutEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}