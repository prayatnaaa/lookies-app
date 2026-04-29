package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.partnerExhibition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.painting.GetEventPaintingByIdUseCase
import com.prayatna.lookiesapp.domain.usecase.partner.ApprovePaintingUseCase
import com.prayatna.lookiesapp.domain.usecase.partner.RejectPaintingUseCase
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.state.EventPaintingDetailEffect
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.state.EventPaintingDetailEvent
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.state.EventPaintingDetailUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartnerExhibitionPaintingDetailViewModel @Inject constructor(
    private val getEventPaintingByIdUseCase: GetEventPaintingByIdUseCase,
    private val approvePaintingUseCase: ApprovePaintingUseCase,
    private val rejectPaintingUseCase: RejectPaintingUseCase
): ViewModel() {

    private val _state = MutableStateFlow(EventPaintingDetailUiState())
    val state = _state.asStateFlow()

    private val _event = Channel<EventPaintingDetailEffect>()
    val event = _event.receiveAsFlow()

    fun onEvent(intent: EventPaintingDetailEvent) {
        when (intent) {
            is EventPaintingDetailEvent.Load -> load(intent.id)
            is EventPaintingDetailEvent.Approve -> approve(intent.id)
            is EventPaintingDetailEvent.Reject -> reject(intent.id, intent.reason)
        }
    }

    private fun load(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = getEventPaintingByIdUseCase(id)) {
                is DataResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            data = result.data
                        )
                    }
                }

                is DataResult.Error -> {
                    _state.update { it.copy(isLoading = false, errorMessage = result.error) }

                    _event.send(
                        EventPaintingDetailEffect.ShowError(result.error)
                    )
                }

                else -> Unit
            }
        }
    }

    private fun approve(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(actionLoading = true) }

            when (val result = approvePaintingUseCase(id)) {
                is DataResult.Success -> {
                    _state.update { it.copy(actionLoading = false) }

                    _event.send(
                        EventPaintingDetailEffect.ShowResult("Painting Approved")
                    )
                }

                is DataResult.Error -> {
                    _state.update { it.copy(actionLoading = false) }

                    _event.send(
                        EventPaintingDetailEffect.ShowError(result.error)
                    )
                }

                else -> Unit
            }
        }
    }

    private fun reject(id: String, reason: String) {
        viewModelScope.launch {
            _state.update { it.copy(actionLoading = true) }

            when (val result = rejectPaintingUseCase(id, reason)) {
                is DataResult.Success -> {
                    _state.update { it.copy(actionLoading = false) }

                    _event.send(
                        EventPaintingDetailEffect.ShowResult("Painting Rejected")
                    )
                }

                is DataResult.Error -> {
                    _state.update { it.copy(actionLoading = false) }

                    _event.send(
                        EventPaintingDetailEffect.ShowError(result.error)
                    )
                }

                else -> Unit
            }
        }
    }
}