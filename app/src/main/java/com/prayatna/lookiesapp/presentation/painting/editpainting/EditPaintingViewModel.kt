package com.prayatna.lookiesapp.presentation.painting.editpainting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.painting.AddPaintingParams
import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.domain.usecase.painting.EditPaintingUseCase
import com.prayatna.lookiesapp.presentation.painting.editpainting.state.EditPaintingUiEffect
import com.prayatna.lookiesapp.presentation.painting.editpainting.state.EditPaintingUiEvent
import com.prayatna.lookiesapp.presentation.painting.editpainting.state.EditPaintingUiState
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
class EditPaintingViewModel @Inject constructor(
    private val editPaintingUseCase: EditPaintingUseCase,
    private val paintingRepository: PaintingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val paintingId: Int = savedStateHandle["paintingId"] ?: -1

    private val _uiState = MutableStateFlow(EditPaintingUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<EditPaintingUiEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadMeta()
        if (paintingId != -1) {
            onEvent(EditPaintingUiEvent.LoadPainting(paintingId))
        }
    }

    fun onEvent(event: EditPaintingUiEvent) {
        when (event) {
            is EditPaintingUiEvent.LoadPainting -> loadPainting(event.id)
            is EditPaintingUiEvent.TitleChanged -> _uiState.update { it.copy(title = event.value) }
            is EditPaintingUiEvent.DescriptionChanged -> _uiState.update { it.copy(description = event.value) }
            is EditPaintingUiEvent.YearChanged -> _uiState.update { it.copy(yearCreated = event.value) }
            is EditPaintingUiEvent.PriceChanged -> _uiState.update { it.copy(price = event.value) }
            is EditPaintingUiEvent.HeightChanged -> _uiState.update { it.copy(dimensionHeight = event.value) }
            is EditPaintingUiEvent.WidthChanged -> _uiState.update { it.copy(dimensionWidth = event.value) }
            is EditPaintingUiEvent.SubjectChanged -> _uiState.update { it.copy(subject = event.value) }
            is EditPaintingUiEvent.ArtStyleChanged -> _uiState.update { it.copy(selectedArtStyleId = event.id) }
            is EditPaintingUiEvent.MediumChanged -> _uiState.update { it.copy(selectedMediumId = event.id) }
            is EditPaintingUiEvent.ImageChanged -> _uiState.update { it.copy(bannerUri = event.uri) }
            EditPaintingUiEvent.Submit -> submit()
            EditPaintingUiEvent.DismissDialog -> _uiState.update { it.copy(errorMessage = null, successMessage = null) }
        }
    }

    private fun loadMeta() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMeta = true) }
            val stylesDef = async { paintingRepository.getPaintingArtStyles() }
            val mediumsDef = async { paintingRepository.getPaintingMediums() }

            val styles = stylesDef.await()
            val mediums = mediumsDef.await()

            if (styles is DataResult.Success) _uiState.update { it.copy(artStyles = styles.data) }
            if (mediums is DataResult.Success) _uiState.update { it.copy(mediums = mediums.data) }
            
            _uiState.update { it.copy(isLoadingMeta = false) }
        }
    }

    private fun loadPainting(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = paintingRepository.getPaintingDetail(id)) {
                is DataResult.Success -> {
                    val p = result.data
                    _uiState.update { it.copy(
                        isLoading = false,
                        title = p.title,
                        description = p.description ?: "",
                        yearCreated = p.yearCreated.toString(),
                        price = p.price.toLong().toString(),
                        dimensionHeight = p.dimensionHeight.toLong().toString(),
                        dimensionWidth = p.dimensionWidth.toLong().toString(),
                        subject = p.subject ?: "",
                        selectedArtStyleId = p.artStyleId ?: "",
                        selectedMediumId = p.mediumId,
                        existingImageUrl = p.paintingUrl
                    ) }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun submit() {
        val state = _uiState.value
        if (!state.isValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val params = AddPaintingParams(
                artistId = "", // Handled by service
                title = state.title,
                description = state.description,
                dimensionHeight = state.dimensionHeight.toDouble(),
                dimensionWidth = state.dimensionWidth.toDouble(),
                paintingUrl = "", // Set by service if image uploaded
                subject = state.subject.ifBlank { null },
                yearCreated = state.yearCreated.toInt(),
                artStyle = state.selectedArtStyleId.ifBlank { null },
                medium = state.selectedMediumId,
                price = state.price.toDouble()
            )

            when (val result = editPaintingUseCase(params, paintingId, state.bannerUri)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true, successMessage = result.data) }
                    _effect.emit(EditPaintingUiEffect.ShowToast(result.data))
                    _effect.emit(EditPaintingUiEffect.NavigateBack)
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error) }
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
