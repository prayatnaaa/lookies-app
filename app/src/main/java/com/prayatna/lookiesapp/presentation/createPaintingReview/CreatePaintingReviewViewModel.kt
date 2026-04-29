package com.prayatna.lookiesapp.presentation.createPaintingReview

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.painting.CreatePaintingReviewInput
import com.prayatna.lookiesapp.domain.usecase.painting.CreatePaintingReviewUseCase
import com.prayatna.lookiesapp.domain.usecase.painting.GetEventPaintingByIdUseCase
import com.prayatna.lookiesapp.presentation.createPaintingReview.state.PaintingReviewEffect
import com.prayatna.lookiesapp.presentation.createPaintingReview.state.PaintingReviewEvent
import com.prayatna.lookiesapp.presentation.createPaintingReview.state.PaintingReviewUiState
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
class CreatePaintingReviewViewModel @Inject constructor(
    private val createPaintingReviewUseCase: CreatePaintingReviewUseCase,
    private val getEventPaintingByIdUseCase: GetEventPaintingByIdUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val orderId: String = checkNotNull(savedStateHandle["orderId"])
    private val eventPaintingId: String = checkNotNull(savedStateHandle["eventPaintingId"])

    private val _state = MutableStateFlow(PaintingReviewUiState())
    val state = _state.asStateFlow()

    private val _effect = Channel<PaintingReviewEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        Log.d("CreatePaintingReviewViewModel", "orderId: $orderId, eventPaintingId: $eventPaintingId")
        getEventPainting(eventPaintingId)
    }

    fun onEvent(event: PaintingReviewEvent) {
        when (event) {
            PaintingReviewEvent.BackClicked -> {
                viewModelScope.launch {
                    _effect.send(PaintingReviewEffect.NavigateBack)
                }
            }
            is PaintingReviewEvent.ReviewMessageChanged -> {
                _state.update { it.copy(reviewNotes = event.value) }
            }
            is PaintingReviewEvent.StarRatingChanged -> {
                _state.update { it.copy(reviewStarRating = event.value) }
            }
            PaintingReviewEvent.SubmitClicked -> createReview()
        }
    }

    private fun createReview() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val input = CreatePaintingReviewInput(
                orderId = orderId,
                eventPaintingId = eventPaintingId,
                rating = state.value.reviewStarRating,
                review = state.value.reviewNotes
            )
            when (val result = createPaintingReviewUseCase(input)) {
                is DataResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.error) }
                    _effect.send(PaintingReviewEffect.ShowBottomDropdown(status = "Error", message = result.error))
                }
                is DataResult.Success -> {
                    _state.update { it.copy(isLoading = false, data = result.data) }
                    _effect.send(PaintingReviewEffect.ShowBottomDropdown(status = "Success", message = "Review created successfully!"))
                }
                else -> Unit
            }
        }
    }

    private fun getEventPainting(eventPaintingId: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = getEventPaintingByIdUseCase(eventPaintingId)) {
                is DataResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.error) }
                    _effect.send(PaintingReviewEffect.ShowBottomDropdown(status = "Error", message = result.error))
                }
                is DataResult.Success -> {
                    _state.update { it.copy(isLoading = false, eventPainting = result.data) }
                }
                else -> Unit
            }
        }
    }

}