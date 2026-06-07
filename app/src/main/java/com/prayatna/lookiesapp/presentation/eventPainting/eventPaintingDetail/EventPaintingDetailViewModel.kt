package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.painting.GetEventPaintingByIdUseCase
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.state.EventPaintingDetailEvent
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.state.EventPaintingDetailUiState
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
class EventPaintingDetailViewModel @Inject constructor(
    private val getEventPaintingByIdUseCase: GetEventPaintingByIdUseCase,
): ViewModel() {

    private val _state = MutableStateFlow(EventPaintingDetailUiState())
    val state: StateFlow<EventPaintingDetailUiState> = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<EventPaintingDetailEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun getEventPaintingDetail(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true,
                data = null,
                errorMessage = null
            ) }

            when (val result = getEventPaintingByIdUseCase(id)) {
                is DataResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            data = null,
                            errorMessage = result.error
                        )
                    }
                }
                is DataResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            data = result.data,
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    fun onChatArtistClicked(artistId: String, artistName: String) {
        viewModelScope.launch {
            _uiEvent.emit(
                EventPaintingDetailEvent.NavigateToChat(
                    merchantId = artistId,
                    otherPartyName = artistName
                )
            )
        }
    }
}
