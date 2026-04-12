package com.prayatna.lookiesapp.presentation.checkout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.order.OrderItemInput
import com.prayatna.lookiesapp.domain.model.transaction.ShipmentFee
import com.prayatna.lookiesapp.domain.model.user.UserAddress
import com.prayatna.lookiesapp.domain.usecase.event.GetEventByIdUseCase
import com.prayatna.lookiesapp.domain.usecase.painting.GetEventPaintingByIdUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.CreateOrderUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.GetDetailTransactionUseCase
import com.prayatna.lookiesapp.domain.usecase.transaction.GetShipmentFeeUseCase
import com.prayatna.lookiesapp.domain.usecase.user.GetUserAddressesUseCase
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutItemDisplay
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutUiState
import com.prayatna.lookiesapp.presentation.checkout.state.PaymentMethodUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val getUserAddressesUseCase: GetUserAddressesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    fun getDetailItem(type: String, id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (type) {
                "event_ticket" -> {
                    handleEventFetch(id)
                }
                "painting" -> {
                    handlePaintingFetch(id)
                    fetchShipmentFees()
                    fetchUserAddresses()
                }
                "event_registration" -> {
                    handleEventRegistrationFetch(id)
                }
                else -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Invalid item type!")
                    }
                }
            }
        }
    }

    private suspend fun handleEventFetch(id: String) {
        when (val result = getEventByIdUseCase(id)) {
            is DataResult.Success -> {
                val event = result.data
                val itemDisplay = CheckoutItemDisplay(
                    id = event.id,
                    title = event.title,
                    subtitle = "by ${event.organizer.legalName}",
                    price = event.ticketPrice,
                    imageUrl = event.bannerImageUrl,
                    type = "event_ticket",
                    merchantId = event.organizer.id
                )
                _uiState.update { it.copy(isLoading = false, itemToBuy = itemDisplay) }
            }
            is DataResult.Error -> {
                _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
            }
            else -> Unit
        }
    }

    private suspend fun handlePaintingFetch(id: String) {
        when (val result = getEventPaintingByIdUseCase(id)) {
            is DataResult.Success -> {
                val data = result.data
                val itemDisplay = CheckoutItemDisplay(
                    id = data.id,
                    title = data.painting.title,
                    subtitle = "by ${data.participant.artist.fullName}",
                    price = data.finalPrice,
                    imageUrl = data.painting.paintingUrl,
                    type = "painting",
                    merchantId = data.participant.event.organizer.id
                )
                _uiState.update { it.copy(isLoading = false, itemToBuy = itemDisplay) }
            }
            is DataResult.Error -> {
                _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
            }
            else -> Unit
        }
    }

    private suspend fun handleEventRegistrationFetch(orderId: String) {
        when (val result = getDetailTransactionUseCase(orderId)) {
            is DataResult.Success -> {
                val transaction = result.data.transaction
                val firstItem = transaction.items.firstOrNull()
                val itemDisplay = CheckoutItemDisplay(
                    id = transaction.id,
                    title = firstItem?.details?.title ?: "Event Registration",
                    subtitle = "Registration Fee",
                    price = transaction.totalAmount,
                    imageUrl = firstItem?.details?.imageUrl ?: "",
                    type = "event_registration",
                    merchantId = transaction.merchantId
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        itemToBuy = itemDisplay,
                        existingOrderId = orderId
                    )
                }
            }
            is DataResult.Error -> {
                _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
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
                _uiState.update {
                    it.copy(isShipmentLoading = false, errorMessage = result.error)
                }
            }
            else -> Unit
        }
    }

    private suspend fun fetchUserAddresses() {
        _uiState.update { it.copy(isAddressLoading = true) }
        when (val result = getUserAddressesUseCase()) {
            is DataResult.Success -> {
                val addresses = result.data
                _uiState.update {
                    it.copy(
                        isAddressLoading = false,
                        userAddresses = addresses,
                        selectedAddress = addresses.firstOrNull { address -> address.isDefault }
                            ?: addresses.firstOrNull()
                    )
                }
            }
            is DataResult.Error -> {
                _uiState.update {
                    it.copy(isAddressLoading = false, errorMessage = result.error)
                }
            }
            else -> Unit
        }
    }

    fun refreshUserAddresses() {
        viewModelScope.launch {
            fetchUserAddresses()
        }
    }

    fun onShipmentFeeSelected(fee: ShipmentFee) {
        _uiState.update {
            it.copy(selectedShipmentFee = fee)
        }
    }

    fun onAddressSelected(address: UserAddress) {
        _uiState.update {
            it.copy(selectedAddress = address)
        }
    }

    fun onPaymentMethodSelected(method: PaymentMethodUiState) {
        _uiState.update {
            it.copy(selectedMethod = method)
        }
    }

    fun createCheckout(
        items: List<OrderItemInput>
    ) {
        viewModelScope.launch {
            val currentItem = _uiState.value.itemToBuy
            Log.d("CheckoutViewModel", "createCheckout: $currentItem")
            if (currentItem == null) {
                _uiState.update { it.copy(errorMessage = "No item yet!") }
                return@launch
            }

            val shippingCost = if (currentItem.type == "painting") {
                _uiState.value.selectedShipmentFee?.fee?.toDouble() ?: 0.0
            } else {
                0.0
            }

            val selectedAddress = _uiState.value.selectedAddress
            if (currentItem.type == "painting" && selectedAddress == null) {
                _uiState.update { it.copy(errorMessage = "Please select a shipping address") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, errorMessage = null, checkoutSuccessData = null) }

            val result = createOrderUseCase(
                items = items,
                shippingCost = shippingCost,
                recipientName = selectedAddress?.name ?: "",
                phoneNumber = selectedAddress?.phoneNumber?: "",
                addressLine = selectedAddress?.addressLine ?: "",
                province = selectedAddress?.province ?: "",
                postalCode = selectedAddress?.postalCode ?: "",
            )

            _uiState.update { currentState ->
                when (result) {
                    is DataResult.Success -> {
                        currentState.copy(
                            isLoading = false,
                            checkoutSuccessData = result.data
                        )
                    }
                    is DataResult.Error -> {
                        currentState.copy(
                            isLoading = false,
                            errorMessage = result.error
                        )
                    }
                    else -> currentState.copy(isLoading = false)
                }
            }
        }
    }

    fun onCheckoutResultConsumed() {
        _uiState.update {
            it.copy(
                errorMessage = null,
                checkoutSuccessData = null
            )
        }
    }
}