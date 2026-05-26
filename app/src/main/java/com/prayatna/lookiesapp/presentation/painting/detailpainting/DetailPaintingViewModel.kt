package com.prayatna.lookiesapp.presentation.painting.detailpainting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.presentation.painting.detailpainting.state.DetailPaintingUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPaintingViewModel @Inject constructor(
    private val paintingRepository: PaintingRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(DetailPaintingUiState())
    val uiState: StateFlow<DetailPaintingUiState> = _uiState.asStateFlow()

    fun loadDetailPainting(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when(val result = paintingRepository.getPaintingDetail(id)) {
                is DataResult.Error -> _uiState.update { it.copy(
                    error = result.error,
                    isLoading = false
                ) }
                is DataResult.Success -> _uiState.update {
                    it.copy(
                        error = null,
                        isLoading = false,
                        painting = result.data
                    )
                }
                else -> Unit
            }
        }
    }
}