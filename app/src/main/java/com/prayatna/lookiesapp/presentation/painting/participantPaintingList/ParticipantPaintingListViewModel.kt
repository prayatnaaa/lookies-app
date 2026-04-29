package com.prayatna.lookiesapp.presentation.painting.participantPaintingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.painting.GetEventPaintingsUseCase
import com.prayatna.lookiesapp.domain.usecase.partner.ApprovePaintingUseCase
import com.prayatna.lookiesapp.domain.usecase.partner.RejectPaintingUseCase
import com.prayatna.lookiesapp.presentation.painting.participantPaintingList.state.DialogState
import com.prayatna.lookiesapp.presentation.painting.participantPaintingList.state.ParticipantPaintingListUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParticipantPaintingListViewModel @Inject constructor(
    private val getEventPaintingsUseCase: GetEventPaintingsUseCase,
    private val approvePaintingUseCase: ApprovePaintingUseCase,
    private val rejectPaintingUseCase: RejectPaintingUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(ParticipantPaintingListUiState())
    val uiState: StateFlow<ParticipantPaintingListUiState> = _uiState.asStateFlow()

    fun loadPaintings(eventId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getEventPaintingsUseCase(eventId)) {
                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.error,
                            isLoading = false
                        )
                    }
                }
                is DataResult.Success -> {
                    _uiState.update {
                        it.copy(
                            eventPaintings = result.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    fun approvePainting(id: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loadingPaintingId = id,
                    dialogState = null
                )
            }

            when (val result = approvePaintingUseCase(id)) {
                is DataResult.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            loadingPaintingId = null,
                            isSuccess = true,
                            eventPaintings = state.eventPaintings.map {
                                if (it.id == id) {
                                    it.copy(status = "accepted")
                                } else it
                            }
                        )
                    }
                }
                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            loadingPaintingId = null,
                            errorMessage = result.error
                        )
                    }
                }
                else -> Unit
            }
        }
    }


    fun rejectPainting(id: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loadingPaintingId = id,
                    dialogState = null
                )
            }

            when (val result = rejectPaintingUseCase(id, reason = "Painting rejected")) {
                is DataResult.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            loadingPaintingId = null,
                            isSuccess = true,
                            eventPaintings = state.eventPaintings.map {
                                if (it.id == id) {
                                    it.copy(status = "rejected")
                                } else it
                            }
                        )
                    }
                }
                is DataResult.Error -> {
                    _uiState.update {
                        it.copy(
                            loadingPaintingId = null,
                            errorMessage = result.error
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    fun showApproveDialog(id: String) {
        _uiState.update { it.copy(dialogState = DialogState.Approve(id)) }
    }

    fun showRejectDialog(id: String) {
        _uiState.update { it.copy(dialogState = DialogState.Reject(id)) }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(dialogState = null) }
    }

}