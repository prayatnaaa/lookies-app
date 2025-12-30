package com.prayatna.lookiesapp.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                    subtitle = "by ${event.organizerId}",
                    price = event.ticketPrice,
                    imageUrl = event.bannerImageUrl,
                    type = "event_ticket"
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
        when (val result = paintingRepository.getPaintingDetail(id.toInt())) {
            is DataResult.Success -> {
                val painting = result.data
                val itemDisplay = CheckoutItemDisplay(
                    id = painting.id.toString(),
                    title = painting.title,
                    subtitle = "by ${painting.artistName}",
                    price = 0.9,
                    imageUrl = painting.paintingUrl,
                    type = "painting"
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
        totalAmount: Double,
        type: String,
        description: String?,
    ) {
        viewModelScope.launch {
            val currentItem = _uiState.value.itemToBuy

            if (currentItem == null) {
                _uiState.update { it.copy(errorMessage = "Data item belum siap.") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, errorMessage = null, checkoutSuccessData = null) }


            val result = transactionRepository.createOrder(
                totalAmount = totalAmount,
                orderType = type,
                description = description,
                transactionType = type
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
        _uiState.update { it.copy(checkoutSuccessData = null, errorMessage = null) }
    }
}