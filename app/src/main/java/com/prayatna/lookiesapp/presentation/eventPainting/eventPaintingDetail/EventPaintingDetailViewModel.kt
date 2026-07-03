package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.painting.GetEventPaintingByIdUseCase
import com.prayatna.lookiesapp.domain.usecase.painting.GetPaintingReviewByEventPaintingIdUseCase
import com.prayatna.lookiesapp.domain.usecase.painting.MarkEventPaintingAsUnsoldUseCase
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.state.EventPaintingDetailEvent
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.state.EventPaintingDetailUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
    private val markEventPaintingAsUnsoldUseCase: MarkEventPaintingAsUnsoldUseCase,
    private val getPaintingReviewByEventPaintingIdUseCase: GetPaintingReviewByEventPaintingIdUseCase
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
                paintingReview = null,
                errorMessage = null
            ) }

            coroutineScope {
                val detailDeferred = async { getEventPaintingByIdUseCase(id) }
                val reviewDeferred = async { getPaintingReviewByEventPaintingIdUseCase(id) }

                val detailResult = detailDeferred.await()
                val reviewResult = reviewDeferred.await()

                if (detailResult is DataResult.Error) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            data = null,
                            errorMessage = detailResult.error
                        )
                    }
                } else if (detailResult is DataResult.Success) {
                    val paintingReview = if (reviewResult is DataResult.Success) reviewResult.data else null
                    
                    _state.update {
                        it.copy(
                            isLoading = false,
                            data = detailResult.data,
                            paintingReview = paintingReview
                        )
                    }
                }
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

    fun markAsUnsold(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(actionLoading = true) }
            when (val result = markEventPaintingAsUnsoldUseCase(id)) {
                is DataResult.Success -> {
                    _state.update { it.copy(actionLoading = false) }
                    // Reload data to reflect changes
                    getEventPaintingDetail(id)
                }
                is DataResult.Error -> {
                    _state.update { it.copy(actionLoading = false, errorMessage = result.error) }
                }
                else -> {
                    _state.update { it.copy(actionLoading = false) }
                }
            }
        }
    }
}
