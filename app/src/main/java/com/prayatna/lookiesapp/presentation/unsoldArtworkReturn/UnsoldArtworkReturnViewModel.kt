package com.prayatna.lookiesapp.presentation.unsoldArtworkReturn

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.shipment.CreateExhibitionShipmentInput
import com.prayatna.lookiesapp.domain.model.shipment.DeliveryMethod
import com.prayatna.lookiesapp.domain.model.shipment.ShipmentStatus
import com.prayatna.lookiesapp.domain.model.shipment.ShipmentType
import com.prayatna.lookiesapp.domain.usecase.merchant.GetMerchantAddressUseCase
import com.prayatna.lookiesapp.domain.usecase.shipment.CreateExhibitionShipmentUseCase
import com.prayatna.lookiesapp.domain.usecase.shipment.GetExhibitionShipmentByEventPaintingIdUseCase
import com.prayatna.lookiesapp.domain.usecase.shipment.UpdateExhibitionShipmentStatusUseCase
import com.prayatna.lookiesapp.presentation.unsoldArtworkReturn.state.UnsoldArtworkReturnEvent
import com.prayatna.lookiesapp.presentation.unsoldArtworkReturn.state.UnsoldArtworkReturnUiState
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UnsoldArtworkReturnViewModel @Inject constructor(
    private val getExhibitionShipmentByEventPaintingIdUseCase: GetExhibitionShipmentByEventPaintingIdUseCase,
    private val getMerchantAddressUseCase: GetMerchantAddressUseCase,
    private val createExhibitionShipmentUseCase: CreateExhibitionShipmentUseCase,
    private val updateExhibitionShipmentStatusUseCase: UpdateExhibitionShipmentStatusUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UnsoldArtworkReturnUiState())
    val uiState: StateFlow<UnsoldArtworkReturnUiState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<String>("eventPaintingId")?.let { id ->
            onEvent(UnsoldArtworkReturnEvent.Init(id))
        }
    }

    fun onEvent(event: UnsoldArtworkReturnEvent) {
        when (event) {
            is UnsoldArtworkReturnEvent.Init -> loadInitialData(event.eventPaintingId)
            is UnsoldArtworkReturnEvent.OnCourierNameChanged -> _uiState.update { it.copy(courierNameInput = event.name) }
            is UnsoldArtworkReturnEvent.OnTrackingNumberChanged -> _uiState.update { it.copy(trackingNumberInput = event.trackingNumber) }
            is UnsoldArtworkReturnEvent.OnNotesChanged -> _uiState.update { it.copy(notesInput = event.notes) }
            UnsoldArtworkReturnEvent.SubmitReturnShipment -> submitReturnShipment()
            UnsoldArtworkReturnEvent.ConfirmArtworkReturned -> confirmArtworkReturned()
            UnsoldArtworkReturnEvent.DismissError -> _uiState.update { it.copy(errorMessage = null) }
            UnsoldArtworkReturnEvent.DismissSuccess -> _uiState.update { it.copy(successMessage = null) }
            UnsoldArtworkReturnEvent.OnBackClick -> { /* Handled in Route */ }
        }
    }

    private fun loadInitialData(eventPaintingId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            when (val shipmentResult = getExhibitionShipmentByEventPaintingIdUseCase(eventPaintingId)) {
                is DataResult.Success -> {
                    val data = shipmentResult.data
                    val artistBusinessId = data.eventPainting.artistId
                    
                    val addressResult = getMerchantAddressUseCase(artistBusinessId)
                    
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            eventPainting = data.eventPainting,
                            shipment = data.exhibitionShipment,
                            paintingTitle = data.eventPainting.painting.title,
                            artistName = data.eventPainting.participant.artist.fullName ?: "Unknown Artist",
                            eventTitle = data.eventPainting.participant.event.title,
                            returnAddress = if (addressResult is DataResult.Success) addressResult.data else null
                        )
                    }
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = shipmentResult.error) }
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun submitReturnShipment() {
        val state = _uiState.value
        val ep = state.eventPainting ?: return

        if (state.trackingNumberInput.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter the return tracking number") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }

            val input = CreateExhibitionShipmentInput(
                eventPaintingId = UUID.fromString(ep.id),
                artistId = UUID.fromString(ep.artistId),
                organizerId = UUID.fromString(ep.participant.event.organizer.id),
                shipmentType = ShipmentType.RETURN,
                deliveryMethod = DeliveryMethod.COURIER,
                trackingNumber = state.trackingNumberInput,
                courierName = state.courierNameInput.takeIf { it.isNotBlank() },
                status = ShipmentStatus.ON_THE_WAY,
                eventId = ep.eventId.toInt()
            )

            when (val result = createExhibitionShipmentUseCase(input)) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isSubmitting = false, successMessage = "Return shipment initiated successfully") }
                    loadInitialData(ep.id)
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isSubmitting = false, errorMessage = result.error) }
                }
                else -> Unit
            }
        }
    }

    private fun confirmArtworkReturned() {
        val state = _uiState.value
        val shipmentId = state.shipment?.id?.toString() ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }

            val result = updateExhibitionShipmentStatusUseCase(
                shipmentId = shipmentId,
                notes = state.notesInput.takeIf { it.isNotBlank() },
                status = "returned_to_artist"
            )
            
            when (result) {
                is DataResult.Success -> {
                    _uiState.update { it.copy(isSubmitting = false, successMessage = "Artwork receipt confirmed") }
                    loadInitialData(state.eventPainting?.id ?: "")
                }
                is DataResult.Error -> {
                    _uiState.update { it.copy(isSubmitting = false, errorMessage = result.error) }
                }
                else -> Unit
            }
        }
    }
}
