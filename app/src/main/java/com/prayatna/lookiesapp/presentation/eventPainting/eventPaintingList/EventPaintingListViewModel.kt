package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.usecase.painting.GetPaintingUseCase
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingList.state.EventPaintingListUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaintingListViewModel @Inject constructor(
    private val getPaintingUseCase: GetPaintingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventPaintingListUiState())
    val uiState = _uiState.asStateFlow()

    fun getPaintings(forceRefresh: Boolean = false) {
        if (!forceRefresh && _uiState.value.paintings.isNotEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = getPaintingUseCase(status = "accepted")

            when (result) {
                is DataResult.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            paintings = result.data,
                            filteredPaintings = result.data,
                            errorMessage = null
                        )
                    }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        filterPaintings(query)
    }

    private fun filterPaintings(query: String) {
        val currentList = _uiState.value.paintings

        if (query.isBlank()) {
            _uiState.update { it.copy(filteredPaintings = currentList) }
            return
        }

        val filtered = currentList.filter { item ->
            item.painting.title.contains(query, ignoreCase = true) ||
                    item.participant.artist.fullName?.contains(query, ignoreCase = true) == true ||
                    item.participant.event.title.contains(query, ignoreCase = true) ||
                    item.painting.subject?.contains(query, ignoreCase = true) == true
        }

        _uiState.update { it.copy(filteredPaintings = filtered) }
    }

    fun retry() = getPaintings(forceRefresh = true)
}