package com.prayatna.lookiesapp.presentation.painting.uploadpainting

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.painting.AddPaintingParams
import com.prayatna.lookiesapp.domain.repository.PaintingRepository
import com.prayatna.lookiesapp.domain.usecase.painting.UploadPaintingUseCase
import com.prayatna.lookiesapp.presentation.painting.uploadpainting.event.UploadPaintingEvent
import com.prayatna.lookiesapp.presentation.painting.uploadpainting.state.PaintingDropdownState
import com.prayatna.lookiesapp.presentation.painting.uploadpainting.state.UploadPaintingUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadPaintingViewModel @Inject constructor(
    private val uploadPaintingUseCase: UploadPaintingUseCase,
    private val paintingRepository: PaintingRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var dropdownState by mutableStateOf(PaintingDropdownState())
        private set
    private val businessId: String = checkNotNull(savedStateHandle["businessId"]) {
        "businessId argument is required to upload a painting"
    }

    var params by mutableStateOf(
        AddPaintingParams(
            artistId = businessId,
            title = "",
            paintingUrl = "",
            description = "",
            dimensionHeight = 0.0,
            dimensionWidth = 0.0,
            medium = "",
            yearCreated = 0,
            price = 0.0
        )
    )
        private set

    var selectedImage by mutableStateOf<Uri?>(null)
        private set

    var uiState by mutableStateOf<UploadPaintingUiState>(UploadPaintingUiState.Idle)
        private set

    init {
        fetchDropdowns()
    }

    private fun fetchDropdowns() {
        viewModelScope.launch {
            dropdownState = dropdownState.copy(isLoading = true)

            val artStylesResult = paintingRepository.getPaintingArtStyles()
            val mediumsResult = paintingRepository.getPaintingMediums()

            dropdownState = dropdownState.copy(
                artStyles = (artStylesResult as? DataResult.Success)?.data ?: emptyList(),
                mediums = (mediumsResult as? DataResult.Success)?.data ?: emptyList(),
                isLoading = false,
                error = (artStylesResult as? DataResult.Error)?.error
                    ?: (mediumsResult as? DataResult.Error)?.error
            )
        }
    }

    fun onEvent(event: UploadPaintingEvent) {
        when(event) {
            is UploadPaintingEvent.OnTitleChange ->
                params = params.copy(title = event.value)

            is UploadPaintingEvent.OnDescriptionChange ->
                params = params.copy(description = event.value)

            is UploadPaintingEvent.OnHeightChange ->
                params = params.copy(dimensionHeight = event.value.toDoubleOrNull() ?: 0.0)

            is UploadPaintingEvent.OnWidthChange ->
                params = params.copy(dimensionWidth = event.value.toDoubleOrNull() ?: 0.0)

            is UploadPaintingEvent.OnMediumChange ->
                params = params.copy(medium = event.value.id)

            is UploadPaintingEvent.OnArtStyleChange ->
                params = params.copy(artStyle = event.value.id)

            is UploadPaintingEvent.OnSubjectChange ->
                params = params.copy(subject = event.value.takeIf { it.isNotBlank() })

            is UploadPaintingEvent.OnYearChange ->
                params = params.copy(yearCreated = event.value.toIntOrNull() ?: 0)

            is UploadPaintingEvent.OnImageSelected ->
                selectedImage = event.uri

            UploadPaintingEvent.ValidateAndSubmit -> validateAndSubmit()

            UploadPaintingEvent.DismissDialog ->
                uiState = UploadPaintingUiState.Idle

            is UploadPaintingEvent.OnPriceChange -> {
                params = params.copy(price = event.value.toDoubleOrNull() ?: 0.0)
            }
        }
    }


    private fun validateAndSubmit() {
        if (params.title.isBlank()) {
            uiState = UploadPaintingUiState.Error("Title cannot be empty")
            return
        }
        if (params.medium.isBlank()) {
            uiState = UploadPaintingUiState.Error("Medium cannot be empty")
            return
        }
        if (params.dimensionHeight <= 0 || params.dimensionWidth <= 0) {
            uiState = UploadPaintingUiState.Error("Dimensions must be greater than 0")
            return
        }
        if (params.yearCreated <= 100) {
            uiState = UploadPaintingUiState.Error("Invalid year")
            return
        }
        if (selectedImage == null) {
            uiState = UploadPaintingUiState.Error("Image is required")
            return
        }

        submitUpload()
    }

    private fun submitUpload() {
        uiState = UploadPaintingUiState.Loading
        viewModelScope.launch {
            try {
                when(val result = uploadPaintingUseCase(params, selectedImage!!)) {
                    is DataResult.Success -> {
                        uiState = UploadPaintingUiState.Success()
                    }
                    is DataResult.Error -> {
                        uiState = UploadPaintingUiState.Error(result.error)
                    }
                    else -> Unit
                }
            } catch (e: Exception) {
                uiState = UploadPaintingUiState.Error(e.message ?: "Unexpected Error")
            }
        }
    }

}
