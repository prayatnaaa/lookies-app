package com.prayatna.lookiesapp.presentation.partner.orderDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.transaction.GetOrderDetailUseCase
import com.prayatna.lookiesapp.presentation.partner.orderDetail.state.PartnerOrderDetailEffect
import com.prayatna.lookiesapp.presentation.partner.orderDetail.state.PartnerOrderDetailEvent
import com.prayatna.lookiesapp.presentation.partner.orderDetail.state.PartnerOrderDetailUiState
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
class PartnerOrderDetailViewModel @Inject constructor(
    private val getOrderDetailUseCase: GetOrderDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderId: String = savedStateHandle["orderId"] ?: ""

    private val _uiState = MutableStateFlow(PartnerOrderDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<PartnerOrderDetailEffect>()
    val effect = _effect.asSharedFlow()

    init {
        if (orderId.isNotEmpty()) {
            loadDetail(orderId)
        }
    }

    fun onEvent(event: PartnerOrderDetailEvent) {
        when (event) {
            is PartnerOrderDetailEvent.LoadOrderDetail -> loadDetail(event.orderId)
            PartnerOrderDetailEvent.OnBackClicked -> {
                viewModelScope.launch {
                    _effect.emit(PartnerOrderDetailEffect.NavigateBack)
                }
            }
            PartnerOrderDetailEvent.OnFinishClicked -> handleFinish()
        }
    }

    private fun handleFinish() {
        val artworkId = _uiState.value.order?.items?.firstOrNull { it.itemType == "painting" }?.itemRefId
        viewModelScope.launch {
            if (artworkId != null) {
                _effect.emit(PartnerOrderDetailEffect.NavigateToArtworkDetail(artworkId))
            } else {
                _effect.emit(PartnerOrderDetailEffect.NavigateBack)
            }
        }
    }

    private fun loadDetail(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getOrderDetailUseCase(id)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, order = result.data) }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
