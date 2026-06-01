package com.prayatna.lookiesapp.presentation.partner.eventTransactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.transaction.GetPaidOrderItemsByEventIdUseCase
import com.prayatna.lookiesapp.presentation.partner.eventTransactions.state.EventTransactionListEffect
import com.prayatna.lookiesapp.presentation.partner.eventTransactions.state.EventTransactionListEvent
import com.prayatna.lookiesapp.presentation.partner.eventTransactions.state.EventTransactionListUiState
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
class EventTransactionListViewModel @Inject constructor(
    private val getPaidOrderItemsByEventIdUseCase: GetPaidOrderItemsByEventIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val eventId: Int = savedStateHandle.get<String>("eventId")?.toIntOrNull() ?: 0

    private val _uiState = MutableStateFlow(EventTransactionListUiState(eventId = eventId))
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<EventTransactionListEffect>()
    val effect = _effect.asSharedFlow()

    init {
        if (eventId != 0) {
            loadItems(eventId)
        }
    }

    fun onEvent(event: EventTransactionListEvent) {
        when (event) {
            is EventTransactionListEvent.LoadItems -> loadItems(event.eventId)
            EventTransactionListEvent.OnBackClicked -> {
                viewModelScope.launch { _effect.emit(EventTransactionListEffect.NavigateBack) }
            }
            is EventTransactionListEvent.OnItemClicked -> {
                viewModelScope.launch { _effect.emit(EventTransactionListEffect.NavigateToOrderDetail(event.orderId)) }
            }
        }
    }

    private fun loadItems(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getPaidOrderItemsByEventIdUseCase(id)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, items = result.data) }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
