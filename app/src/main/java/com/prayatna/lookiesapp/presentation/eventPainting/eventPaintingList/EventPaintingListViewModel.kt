package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.domain.usecase.painting.GetPaintingUseCase
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingList.state.EventPaintingListUiEffect
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingList.state.EventPaintingListUiEvent
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingList.state.EventPaintingListUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaintingListViewModel @Inject constructor(
    private val getPaintingUseCase: GetPaintingUseCase,
    private val paintingRepository: PaintingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventPaintingListUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<EventPaintingListUiEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadInitialData()
        getPaintings()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val artStylesDeferred = async { paintingRepository.getPaintingArtStyles() }
            val mediumsDeferred = async { paintingRepository.getPaintingMediums() }

            val artStylesResult = artStylesDeferred.await()
            if (artStylesResult is DataResult.Success) {
                _uiState.update { it.copy(artStyles = artStylesResult.data) }
            }

            val mediumsResult = mediumsDeferred.await()
            if (mediumsResult is DataResult.Success) {
                _uiState.update { it.copy(mediums = mediumsResult.data) }
            }
        }
    }

    fun getPaintings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Using "accepted" status implicitly via backend view or parameter if supported
            // The requirement is to filter based on UI selection
            val result = getPaintingUseCase(status = _uiState.value.selectedStatus)
            
            when (result) {
                is DataResult.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            paintings = result.data,
                            errorMessage = null
                        )
                    }
                    applyFilters()
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onEvent(event: EventPaintingListUiEvent) {
        when (event) {
            is EventPaintingListUiEvent.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                applyFilters()
            }
            EventPaintingListUiEvent.OnSearchTriggered -> {
                getPaintings()
            }
            is EventPaintingListUiEvent.OnStatusSelected -> {
                val newStatus = if (_uiState.value.selectedStatus == event.status) null else event.status
                _uiState.update { it.copy(selectedStatus = newStatus) }
                getPaintings()
            }
            is EventPaintingListUiEvent.OnArtStyleSelected -> {
                _uiState.update { it.copy(selectedArtStyle = event.style) }
            }
            is EventPaintingListUiEvent.OnMediumSelected -> {
                _uiState.update { it.copy(selectedMedium = event.medium) }
            }
            EventPaintingListUiEvent.OnSortPriceToggled -> {
                _uiState.update { it.copy(isPriceAscending = !it.isPriceAscending) }
                applyFilters()
            }
            EventPaintingListUiEvent.OnFilterSheetToggle -> {
                _uiState.update { it.copy(isFilterSheetOpen = !it.isFilterSheetOpen) }
            }
            EventPaintingListUiEvent.OnApplyFilters -> {
                _uiState.update { it.copy(isFilterSheetOpen = false) }
                applyFilters()
            }
            EventPaintingListUiEvent.OnResetFilters -> {
                _uiState.update { it.copy(
                    selectedArtStyle = null,
                    selectedMedium = null,
                    isFilterSheetOpen = false
                ) }
                applyFilters()
            }
            EventPaintingListUiEvent.OnRetry -> getPaintings()
        }
    }

    private fun applyFilters() {
        val state = _uiState.value
        val query = state.searchQuery
        val currentList = state.paintings

        var filtered = if (query.isBlank()) {
            currentList
        } else {
            currentList.filter { item ->
                item.painting.title.contains(query, ignoreCase = true) ||
                        item.participant.artist.fullName?.contains(query, ignoreCase = true) == true ||
                        item.participant.event.title.contains(query, ignoreCase = true) ||
                        item.painting.subject?.contains(query, ignoreCase = true) == true
            }
        }

        // Apply Art Style Filter
        state.selectedArtStyle?.let { style ->
            filtered = filtered.filter { it.painting.artStyle == style }
        }

        // Apply Medium Filter
        state.selectedMedium?.let { medium ->
            filtered = filtered.filter { it.painting.medium == medium }
        }

        // Apply Sorting
        filtered = if (state.isPriceAscending) {
            filtered.sortedBy { it.painting.price }
        } else {
            filtered.sortedByDescending { it.painting.price }
        }

        _uiState.update { it.copy(filteredPaintings = filtered) }
    }
}
