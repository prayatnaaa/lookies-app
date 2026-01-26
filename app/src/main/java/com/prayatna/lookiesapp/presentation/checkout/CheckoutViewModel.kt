package com.prayatna.lookiesapp.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.order.OrderItemInput
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.domain.repository.TransactionRepository
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutItemDisplay
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutUiState
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
    private val transactionRepository: TransactionRepository,
    private val eventRepository: EventRepository,
    private val paintingRepository: PaintingRepository
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
                }
                else -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Tipe item tidak valid")
                    }
                }
            }
        }
    }

    private suspend fun handleEventFetch(id: String) {
        when (val result = eventRepository.getEvent(id)) {
            is DataResult.Success -> {
                val event = result.data
                val itemDisplay = CheckoutItemDisplay(
                    id = event.id,
                    title = event.title,
                    subtitle = "by ${event.organizer.name}",
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
        when (val result = paintingRepository.getEventPaintingDetail(id)) {
            is DataResult.Success -> {
                val data = result.data
                val itemDisplay = CheckoutItemDisplay(
                    id = data.id,
                    title = data.painting.title,
                    subtitle = "by ${data.participant.artist.fullName}",
                    price = data.finalPrice,
                    imageUrl = data.painting.paintingUrl,
                    type = "painting",
                    merchantId = data.participant.id
                )
                _uiState.update { it.copy(isLoading = false, itemToBuy = itemDisplay) }
            }
            is DataResult.Error -> {
                _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
            }
            else -> Unit
        }
    }

    fun createCheckout(
        items: List<OrderItemInput>
    ) {
        viewModelScope.launch {
            val currentItem = _uiState.value.itemToBuy

            if (currentItem == null) {
                _uiState.update { it.copy(errorMessage = "Data item belum siap.") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, errorMessage = null, checkoutSuccessData = null) }

            val result = transactionRepository.createOrder(
                items = items
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