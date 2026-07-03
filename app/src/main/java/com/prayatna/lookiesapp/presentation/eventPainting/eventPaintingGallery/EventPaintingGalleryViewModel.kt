package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingGallery

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.painting.GetPaintingUseCase
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingGallery.state.EventPaintingGalleryUiEffect
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingGallery.state.EventPaintingGalleryUiEvent
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingGallery.state.EventPaintingGalleryUiState
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
class EventPaintingGalleryViewModel @Inject constructor(
    private val getPaintingUseCase: GetPaintingUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val eventId: String = savedStateHandle["eventId"] ?: ""

    private val _uiState = MutableStateFlow(EventPaintingGalleryUiState(eventId = eventId))
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<EventPaintingGalleryUiEffect>()
    val effect = _effect.asSharedFlow()

    init {
        if (eventId.isNotEmpty()) {
            onEvent(EventPaintingGalleryUiEvent.LoadPaintings(eventId))
        }
    }

    fun onEvent(event: EventPaintingGalleryUiEvent) {
        when (event) {
            is EventPaintingGalleryUiEvent.LoadPaintings -> loadPaintings(event.eventId)
            is EventPaintingGalleryUiEvent.OnPaintingClicked -> {
                viewModelScope.launch {
                    _effect.emit(EventPaintingGalleryUiEffect.NavigateToDetail(event.paintingId))
                }
            }
            EventPaintingGalleryUiEvent.OnBackClicked -> {
                viewModelScope.launch {
                    _effect.emit(EventPaintingGalleryUiEffect.NavigateBack)
                }
            }
        }
    }

    private fun loadPaintings(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getPaintingUseCase(eventId = id, status = "on_sale, sold")) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, paintings = result.data) }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
