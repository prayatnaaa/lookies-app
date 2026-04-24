package com.prayatna.lookiesapp.presentation.exhibitionHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.painting.EventPainting
import com.prayatna.lookiesapp.domain.usecase.artist.GetArtistEventPaintingsUseCase
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExhibitionHistoryUiState(
    val isLoading: Boolean = false,
    val paintings: List<EventPainting> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class ExhibitionHistoryViewModel @Inject constructor(
    private val getArtistEventPaintingsUseCase: GetArtistEventPaintingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExhibitionHistoryUiState())
    val uiState: StateFlow<ExhibitionHistoryUiState> = _uiState.asStateFlow()

    fun loadPaintings(artistUserId: String) {
        if (_uiState.value.paintings.isNotEmpty()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getArtistEventPaintingsUseCase(artistUserId)) {
                is DataResult.Success -> _uiState.update {
                    it.copy(isLoading = false, paintings = result.data)
                }
                is DataResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.error)
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun retry(artistUserId: String) {
        _uiState.update { it.copy(paintings = emptyList()) }
        loadPaintings(artistUserId)
    }
}
