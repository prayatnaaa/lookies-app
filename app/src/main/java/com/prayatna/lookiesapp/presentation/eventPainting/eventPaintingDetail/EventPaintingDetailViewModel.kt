package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.state.EventPaintingDetailUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventPaintingDetailViewModel @Inject constructor(
    private val paintingRepository: PaintingRepository
): ViewModel() {

    private val _state = MutableStateFlow(EventPaintingDetailUiState())
    val state: StateFlow<EventPaintingDetailUiState> = _state.asStateFlow()

    fun getEventPaintingDetail(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true,
                data = null,
                errorMessage = null
            ) }

            when (val result = paintingRepository.getEventPaintingDetail(id)) {
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
}